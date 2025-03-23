import IpcChannels from "../../../common/IpcChannels";
import { AppCfgStore } from "../../config/AppStore";
import { AppRootDirKey } from "../../constant/AppConstant";
import { app, ipcMain } from "electron";

export default function listenAppInfo() {
  ipcMain.handle(IpcChannels.AskAppInfo, async (event, ...args) => {
    return {
      version: app.getVersion(),
      rootDir: AppCfgStore.get(AppRootDirKey)
    };
  });
}
