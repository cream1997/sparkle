import IpcChannels from "../../../common/IpcChannels";
import { BrowserWindow, ipcMain } from "electron";

export default function listenTest(mainWin: BrowserWindow) {
  ipcMain.handle(IpcChannels.Test, async (event, ...args) => {
    console.log("Test");
    return "主进程收到Test命令";
  });
}
