import { ipcMain } from "electron";
import { IpcChannelsOfHero } from "../../../common/IpcChannels.ts";

export default function listenHeroChannels() {
  ipcMain.handle(
    IpcChannelsOfHero.WsConnect,
    async (_event, { uid, token }) => {}
  );
}
