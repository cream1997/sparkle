import { ipcMain } from "electron";
import { IpcChannelsOfHero } from "../../../common/IpcChannels.ts";
import GameWebSocket from "../../net/HeroWebSocket.ts";
import type { WsConnectResult } from "../../../common/types/WsConnectResult.ts";

let gameWebSocket: GameWebSocket | null = null;
export default function listenHeroChannels() {
  ipcMain.handle(
    IpcChannelsOfHero.WsConnect,
    async (_event, { ip, socketPort, token }) => {
      if (gameWebSocket) {
        gameWebSocket.close();
      }
      gameWebSocket = new GameWebSocket(ip, socketPort, token, msg => {});
      const connectResult: WsConnectResult = { success: false, err: null };
      try {
        await gameWebSocket.connect();
        connectResult.success = true;
      } catch (err) {
        connectResult.err = err;
      }
      return connectResult;
    }
  );
}
