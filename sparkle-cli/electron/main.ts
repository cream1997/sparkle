import { app, BrowserWindow } from "electron";
import * as path from "node:path";
import { electronApp, is, optimizer } from "@electron-toolkit/utils";
import mainIpcSetup from "./ipc/MainIpcSetup";
import { AppRootDirKey } from "./constant/AppConstant";
import IpcChannels from "../common/IpcChannels";
import { AppCfgStore } from "./config/AppStore";

let win: BrowserWindow;

function createWin() {
  win = new BrowserWindow({
    width: 800,
    height: 600,
    show: false,
    autoHideMenuBar: true,
    icon: path.join(__dirname, "../../public/icon.png"),
    webPreferences: {
      preload: path.join(__dirname, "./preload.js"),
      sandbox: false
    }
  });
  win.on("ready-to-show", () => {
    win.show();
    if (is.dev) {
      win.webContents.openDevTools();
    }

    const rootDir = AppCfgStore.get(AppRootDirKey);
    if (!rootDir) {
      win.webContents.send(IpcChannels.ToInitPage);
    }
  });

  if (is.dev && process.env.VITE_DEV_SERVER_URL) {
    win.loadURL(process.env.VITE_DEV_SERVER_URL);
  } else {
    win.loadFile("build/renderer/index.html");
  }
}

app.whenReady().then(() => {
  // Set app user model id for windows
  electronApp.setAppUserModelId("com.cream.sparkle");

  // Default open or close DevTools by F12 in development
  // and ignore CommandOrControl + R in production.
  // see https://github.com/alex8088/electron-toolkit/tree/master/packages/utils
  app.on("browser-window-created", (_, window) => {
    optimizer.watchWindowShortcuts(window);
  });

  createWin();
  mainIpcSetup(win);
  app.on("activate", function () {
    // mac特定处理
    if (BrowserWindow.getAllWindows().length === 0) createWin();
  });
});

// mac特定处理
app.on("window-all-closed", () => {
  if (process.platform !== "darwin") {
    app.quit();
  }
});

// 下面可以包括主进程特定代码的其余部分。也可以将它们放在单独的文件中，并在这里引入。
