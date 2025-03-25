import listenTest from "./listeners/listenTest";
import listenDownloadUpdate from "./listeners/listenDownloadUpdate";
import listenSelectRootDir from "./listeners/listenSelectRootDir";
import listenAppInfo from "./listeners/listenAppInfo";

export default function mainIpcSetup() {
  listenTest();
  listenSelectRootDir();
  listenAppInfo();
  listenDownloadUpdate();
}
