import { BrowserWindow, dialog, ipcMain } from "electron";
import IpcChannels from "../common/IpcChannels";
import { AppCfgStore } from "./config/AppStore";
import { AppRootDirKey } from "./constant/AppConstant";

export default function mainIpcSetup(mainWin: BrowserWindow) {
  listenTest(mainWin);
  listenSelectRootDir();
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
