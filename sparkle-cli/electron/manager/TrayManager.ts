import { app, ipcMain, Menu, nativeImage, Tray } from "electron";
import iconDataUrl from "../../public/icon.png?inline";
import { destroyMainWindow, mainWin } from "./WindowManager.ts";
import { destroyFloatWin, toggleFloatWin } from "./FloatWinManager.ts";
import { FloatWinVisibleChange } from "../constant/MainConst.ts";

function createTray() {
  // 托盘管理
  const icon = nativeImage.createFromDataURL(iconDataUrl);
  const tray = new Tray(icon);

  tray.setContextMenu(getContextMenu(false));
  tray.setToolTip("Sparkle");

  tray.on("click", (e) => {
    if (!mainWin.isVisible()) {
      mainWin.show();
      mainWin.setSkipTaskbar(false);
    }
  });
  // 监听悬浮窗状态变化，更新菜单
  ipcMain.on(FloatWinVisibleChange, (winVisible: any) => {
    tray.setContextMenu(getContextMenu(winVisible));
  });
  // 清理事件监听
  app.on("before-quit", () => {
    ipcMain.removeAllListeners(FloatWinVisibleChange);
  });
}

function getContextMenu(winVisible: boolean) {
  return Menu.buildFromTemplate([
    {
      label: winVisible ? "关闭悬浮窗" : "打开悬浮窗",
      type: "normal",
      click: () => {
        toggleFloatWin();
      }
    },
    {
      label: "偏好设置",
      type: "normal"
    },
    {
      label: "退出",
      type: "normal",
      click: () => {
        destroyFloatWin();
        app.quit();
        destroyMainWindow();
      }
    }
  ]);
}

export default createTray;
