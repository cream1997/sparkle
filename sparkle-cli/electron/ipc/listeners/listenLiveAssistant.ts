import { ipcMain } from "electron";
import IpcChannels from "../../../common/IpcChannels.ts";
import { getAppRootDir } from "../../config/AppStore.ts";
import * as path from "node:path";
import * as fs from "node:fs";
import { jiaMi, jieMi } from "../../../common/CommonTools.ts";

let key: string | null;
let filePath: string | null;
export default function listenLiveAssistant() {
  ipcMain.handle(IpcChannels.LiveAssistantGet, (_event, pwd: string) => {
    if (!pwd) {
      throw new Error("Invalid pwd");
    }
    key = pwd;
    const appRootDir = getAppRootDir();
    filePath = path.join(appRootDir, "liveAssistant.info");
    try {
      fs.accessSync(filePath, fs.constants.F_OK);
    } catch (err) {
      // 文件不存在，创建文件
      fs.writeFileSync(filePath, "", { encoding: "utf8" });
    }
    const dataStr = fs.readFileSync(filePath, "utf8");
    if (!dataStr) {
      // 为空就直接返回
      return dataStr;
    } else {
      return jieMi(dataStr, key);
    }
  });

  ipcMain.on(IpcChannels.LiveAssistantSave, (event, data) => {
    if (key && filePath) {
      const str = jiaMi(JSON.stringify(data), key);
      if (str) {
        fs.writeFileSync(filePath, str, { encoding: "utf8" });
      }
    }
  });
}
