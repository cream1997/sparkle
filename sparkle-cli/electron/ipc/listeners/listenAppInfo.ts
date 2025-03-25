import IpcChannels from "../../../common/IpcChannels";
import { AppCfgStore } from "../../config/AppStore";
import { AppRootDirKey, ServerAddressKey } from "../../constant/MainConst.ts";
import { app, ipcMain } from "electron";

export default function listenAppInfo() {
  ipcMain.handle(IpcChannels.AskAppInfo, async (event, ...args) => {
    return {
      version: app.getVersion(),
      rootDir: AppCfgStore.get(AppRootDirKey),
      serverAddress: AppCfgStore.get(ServerAddressKey)
    };
  });
}
