import IpcChannels from "../../../common/IpcChannels";
import { axios } from "../../../src/net/AxiosCfg";
import NetApi from "../../../common/NetApi";
import { AppTmpDir } from "../../constant/AppConstant";
import { ipcMain } from "electron";

export default function listenDownloadUpdate() {
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
