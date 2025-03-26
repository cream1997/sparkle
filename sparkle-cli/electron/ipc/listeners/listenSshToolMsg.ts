import { ipcMain } from "electron";
import IpcChannels from "../../../common/IpcChannels.ts";

export default function listenSshToolMsg() {
  ipcMain.on(
    IpcChannels.SshLogin,
    async (event, { serverIp, port, username, password }) => {}
  );
}
