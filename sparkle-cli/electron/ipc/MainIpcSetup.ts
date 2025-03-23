import { BrowserWindow } from "electron";
import listenTest from "./listeners/listenTest";
import listenDownloadUpdate from "./listeners/listenDownloadUpdate";
import listenSelectRootDir from "./listeners/listenSelectRootDir";
import listenAppInfo from "./listeners/listenAppInfo";

export default function mainIpcSetup(mainWin: BrowserWindow) {
  listenTest(mainWin);
  listenSelectRootDir();
  listenAppInfo();
  listenDownloadUpdate();
}
