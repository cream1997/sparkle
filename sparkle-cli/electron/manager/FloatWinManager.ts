import { BrowserWindow, ipcMain } from "electron";
import { FloatWinVisibleChange } from "../constant/MainConst.ts";
import * as path from "node:path";
import { is } from "@electron-toolkit/utils";
import IpcChannels from "../../common/IpcChannels.ts";

let floatWin: BrowserWindow | null = null;

export function toggleFloatWin() {
  let floatWinVisibleState = true;
  if (floatWin) {
    if (floatWin.isVisible()) {
      floatWin.hide();
      floatWinVisibleState = false;
    } else {
      floatWin.show();
    }
  } else {
    createFloatWin();
  }
  ipcMain.emit(FloatWinVisibleChange, floatWinVisibleState);
}

export function destroyFloatWin() {
  if (floatWin) {
    floatWin.close();
    floatWin = null;
  }
}

function createFloatWin() {
  if (floatWin) {
    floatWin.show();
    return;
  }
  floatWin = new BrowserWindow({
    width: 80,
    height: 30,
    frame: false,
    show: false,
    transparent: true,
    alwaysOnTop: true,
    skipTaskbar: true,
    webPreferences: {
      preload: path.join(__dirname, "./preload.js")
    }
  });

  floatWin.on("ready-to-show", () => {
    if (!floatWin) {
      return;
    }
    // 跳转到浮窗页面
    floatWin.webContents.send(IpcChannels.ToFloatWinPage);
    floatWin.show();
  });

  if (is.dev && process.env.VITE_DEV_SERVER_URL) {
    floatWin.loadURL(process.env.VITE_DEV_SERVER_URL);
  } else {
    floatWin.loadFile("build/renderer/index.html");
  }

  floatWin.on("closed", () => {
    floatWin = null;
  });
}
