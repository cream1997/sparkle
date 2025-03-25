import { BrowserWindow } from "electron";

let mainWin: BrowserWindow;

function initMainWindow(mainWindow: BrowserWindow) {
  mainWin = mainWindow;
}

function destroyMainWindow() {
  //@ts-expect-error 不给mainWin设置BrowserWindow|null，是因为一旦设置，外面导入的地方都需要判空
  mainWin = null;
}

export { mainWin, initMainWindow, destroyMainWindow };
