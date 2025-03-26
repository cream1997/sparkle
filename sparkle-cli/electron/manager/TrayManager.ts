import { app, Menu, nativeImage, Tray } from "electron";
import iconDataUrl from "../../public/icon.png?inline";
import { destroyMainWindow, mainWin } from "./WindowManager.ts";

function createTray() {
  // 托盘管理
  const icon = nativeImage.createFromDataURL(iconDataUrl);
  const tray = new Tray(icon);

  const contextMenu = Menu.buildFromTemplate([
    {
      label: "偏好设置",
      type: "normal"
    },
    {
      label: "退出",
      type: "normal",
      click: () => {
        app.quit();
        destroyMainWindow();
      }
    }
  ]);
  tray.setContextMenu(contextMenu);

  tray.setToolTip("Sparkle");

  tray.on("click", (e) => {
    if (!mainWin.isVisible()) {
      mainWin.show();
      mainWin.setSkipTaskbar(false);
    }
  });
}

export default createTray;
