import { app, BrowserWindow } from "electron";
import * as path from "node:path";
import { electronApp, is, optimizer } from "@electron-toolkit/utils";
import mainIpcSetup from "./ipc/MainIpcSetup";
import IpcChannels from "../common/IpcChannels";
import { getAppRootDir } from "./config/AppStore";
import { initMainWindow } from "./manager/WindowManager.ts";
import createTray from "./manager/TrayManager.ts";

let mainWin: BrowserWindow;

function createWin() {
  mainWin = new BrowserWindow({
    width: 800,
    height: 600,
    show: false,
    autoHideMenuBar: true,
    icon: path.join(__dirname, "../../public/icon.png"),
    // 移除默认的title bar
    titleBarStyle: "hidden",
    webPreferences: {
      preload: path.join(__dirname, "./preload.js"),
      sandbox: false,
      // todo 禁用后台节流，观察效果
      backgroundThrottling: false
    }
  });
  initMainWindow(mainWin);

  mainWin.on("ready-to-show", () => {
    if (!mainWin) {
      return;
    }
    mainWin.show();
    if (is.dev) {
      // mainWin.webContents.openDevTools();
    }

    const rootDir = getAppRootDir();
    if (!rootDir) {
      mainWin.webContents.send(IpcChannels.ToInitPage);
    }
  });

  mainWin.on("close", e => {
    // 阻止窗口关闭
    e.preventDefault();
    // 从任务栏移除
    mainWin.setSkipTaskbar(true);
    // 隐藏窗口
    mainWin.hide();
  });

  if (is.dev && process.env.VITE_DEV_SERVER_URL) {
    mainWin.loadURL(process.env.VITE_DEV_SERVER_URL);
  } else {
    mainWin.loadFile("build/renderer/index.html");
  }
}

const gotTheLock = app.requestSingleInstanceLock();
if (gotTheLock || is.dev) {
  app.whenReady().then(() => {
    // 方便程序分组
    electronApp.setAppUserModelId("com.cream.sparkle");

    createTray();
    // Default open or close DevTools by F12 in development and ignore CommandOrControl + R in production.
    // see https://github.com/alex8088/electron-toolkit/tree/master/packages/utils
    app.on("browser-window-created", (_, window) => {
      optimizer.watchWindowShortcuts(window);
    });

    createWin();
    mainIpcSetup();
    app.on("second-instance", () => {
      if (mainWin) {
        mainWin.restore();
        mainWin.show();
        mainWin.focus();
      }
    });
  });
  // 下面可以包括主进程特定代码的其余部分。也可以将它们放在单独的文件中，并在这里引入。
  // ...
} else {
  app.quit();
}
