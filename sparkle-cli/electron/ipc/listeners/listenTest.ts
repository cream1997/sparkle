import IpcChannels from "../../../common/IpcChannels";
import { ipcMain } from "electron";

export default function listenTest() {
  ipcMain.handle(IpcChannels.Test, async (event, ...args) => {
    console.log("Test");
    return "主进程收到Test命令";
  });
}
