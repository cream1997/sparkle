import IpcChannels from "../../../common/IpcChannels";
import { axios } from "../../../src/net/AxiosCfg";
import NetApi from "../../../common/NetApi";
import { AppTmpDir } from "../../constant/MainConst.ts";
import { app, ipcMain } from "electron";
import * as path from "node:path";
import * as fs from "node:fs";
import { exec } from "child_process";
import * as iconv from "iconv-lite";
import { mainWin } from "../../manager/WindowManager.ts";

export default function listenDownloadUpdate() {
  ipcMain.on(IpcChannels.DownloadUpdate, async (event, versionNumber) => {
    try {
      const response = await axios.get(NetApi.DownloadLatestVersion, {
        responseType: "stream",
        params: { versionNumber }
      });
      const lengthStr = response.headers["content-length"]?.toString() || "0";
      const totalSize = parseInt(lengthStr, 10);
      // 同步信息到渲染进程开始下载
      downloadInfoSync({ totalSize: totalSize });
      let downloadBytes = 0;
      response.data.on("data", (chunk: any) => {
        downloadBytes += chunk.length;
        downloadInfoSync({ downloadBytes });
      });
      const downloadPath = getDownloadPath(versionNumber);
      const writeStream = fs.createWriteStream(downloadPath);
      response.data.pipe(writeStream);

      await new Promise<void>((resolve, reject) => {
        writeStream.on("finish", resolve);
        writeStream.on("error", reject);
      });
      writeStream.close();
      setTimeout(() => {
        // 下载完成退出并安装,延迟一会，因为到100的时候，页面要显示下载完成准备退出程序进行安装，需要让用户能看到
        installUpdate(downloadPath);
      }, 1000);
    } catch (error) {
      console.error("下载失败", error);
      onErr(error as string);
    }
  });
}

function downloadInfoSync(param: {
  downloadBytes?: number;
  totalSize?: number;
  err?: string;
}) {
  mainWin.webContents.send(IpcChannels.DownloadInfoSyn, param);
}

function getDownloadPath(versionNumber: string) {
  return path.join(AppTmpDir, `app-update-${versionNumber}.exe`);
}

/**
 * 因为这个exec它不支持配置为GBK编码(chcp也试过，不行，可能是chcp只是控制控制台的解码显示，但是没有控制到编码输出)，
 * 所以只能配置为接受二进制buffer,然后用iconv来解码，这样总的来说stderr可以正常显示了
 * 但是error还是会有一部分乱码(stack和message中嵌入的的stderr),因为它内部的转码不好控制，所以最终有瑕疵；
 * 不过乱码那一部分，就是stderr的内容；显然这就是这个函数不支持GBK带来的bug,配置为buffer(也就是不确定编码的意思)，不应再将内嵌的消息直接转为string
 * 总之不影响查看具体报错了，error乱码的内容就是stderr，而stderr由于是buffer，我将其进行了转码操作
 */
function installUpdate(downloadPath: string) {
  exec(downloadPath, { encoding: "buffer" }, (error, stdout, stderr) => {
    if (error) {
      const decode = iconv.decode(stderr, "GBK");
      console.error(error);
      onErr(`安装失败${decode}`);
    } else {
      app.quit();
    }
  });
}

/**
 * 更新出错处理，删除临时文件、 发送更新信息到底部信息面板， 包括下载进度，过程是否异常等。
 */
function onErr(err: string) {
  downloadInfoSync({ err });
}
