import { ipcMain } from "electron";
import IpcChannels from "../../../common/IpcChannels.ts";
import { WinOpType } from "../../../common/CommonConst.ts";
import { mainWin } from "../../manager/WindowManager.ts";

export default function listenWinOption() {
  ipcMain.on(IpcChannels.WinOption, (_event, type: WinOpType) => {
    switch (type) {
      case WinOpType.Close: {
        mainWin.close();
        break;
      }
      case WinOpType.Maximize: {
        mainWin.maximize();
        break;
      }
      case WinOpType.Restore: {
        mainWin.unmaximize();
        break;
      }
      case WinOpType.Minimum: {
        mainWin.minimize();
        break;
      }
      case WinOpType.Top: {
        mainWin.setAlwaysOnTop(true);
        break;
      }
      case WinOpType.UnTop: {
        mainWin.setAlwaysOnTop(false);
        break;
      }
      default: {
        console.error("无效的窗口操作类型");
      }
    }
  });
}
