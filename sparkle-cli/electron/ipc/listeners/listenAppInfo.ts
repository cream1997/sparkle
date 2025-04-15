import IpcChannels from "../../../common/IpcChannels";
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
