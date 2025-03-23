import { app, BrowserWindow, dialog, ipcMain } from "electron";
import IpcChannels from "../common/IpcChannels";
import { AppCfgStore } from "./config/AppStore";
import { AppRootDirKey, AppTmpDir } from "./constant/AppConstant";
import { axios } from "../src/net/AxiosCfg";
import NetApi from "../common/NetApi";

export default function mainIpcSetup(mainWin: BrowserWindow) {
  listenTest(mainWin);
  listenSelectRootDir();
  listenAppInfo();
  listenDownloadUpdate();
}

function listenDownloadUpdate() {
  ipcMain.on(IpcChannels.DownloadUpdate, (event, versionNumber) => {
    axios
      .get(NetApi.DownloadLatestVersion, {
        responseType: "stream",
        params: { versionNumber }
      })
      .then((response) => {
        console.log(AppTmpDir);
      })
      .catch((error) => {
        console.error(error);
      });
  });
}

function listenTest(mainWin: BrowserWindow) {
  ipcMain.handle(IpcChannels.Test, async (event, ...args) => {
    console.log("Test");
    return "主进程收到Test命令";
  });
}

function listenSelectRootDir() {
  ipcMain.handle(IpcChannels.SelectRootDir, async () => {
    const result = await dialog.showOpenDialog({
      properties: ["openDirectory"],
      title: "请选择根文件夹"
    });
    if (!result.canceled) {
      const path = result.filePaths[0];
      AppCfgStore.set(AppRootDirKey, path);
      return true;
    }
    return false;
  });
}

function listenAppInfo() {
  ipcMain.handle(IpcChannels.AskAppInfo, async (event, ...args) => {
    return {
      version: app.getVersion(),
      rootDir: AppCfgStore.get(AppRootDirKey)
    };
  });
}
