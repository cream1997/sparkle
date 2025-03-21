import {BrowserWindow, ipcMain} from "electron";
import IpcChannels from "../common/IpcChannels";

export default function mainIpcSetup(mainWin: BrowserWindow) {
    ipcMain.on(IpcChannels.Ping, () => console.log('pong'))
}

