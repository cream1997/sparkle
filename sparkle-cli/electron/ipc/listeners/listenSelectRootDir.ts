import IpcChannels from "../../../common/IpcChannels";
import { AppCfgStore } from "../../config/AppStore";
import { AppRootDirKey } from "../../constant/AppConstant";
import { dialog, ipcMain } from "electron";

export default function listenSelectRootDir() {
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
