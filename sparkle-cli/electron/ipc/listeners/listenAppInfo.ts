import IpcChannels from "../../../common/channels/IpcChannels.ts";
import { getAppRootDir, getServerAddress } from "../../config/AppStore";
import { app, ipcMain } from "electron";

export default function listenAppInfo() {
  ipcMain.handle(IpcChannels.AskAppInfo, async (event, ...args) => {
    return {
      version: app.getVersion(),
      rootDir: getAppRootDir(),
      serverAddress: getServerAddress()
    };
  });
}
