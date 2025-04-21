import { ipcMain } from "electron";
import GameWebSocket from "../../net/HeroWebSocket.ts";
import type { WsConnectResult } from "../../../common/types/WsConnectResult.ts";
import { InvokeListener, SendListener } from "../../types/MainProcessTypes.ts";
import { mainWin } from "../../manager/WindowManager.ts";
import { IpcChannelsOfHero } from "../../../common/channels/IpcChannelsOfHero.ts";

export default function listenHeroChannels() {
  ipcMain.handle(IpcChannelsOfHero.WsConnect, wsConnectListener);
  ipcMain.on(IpcChannelsOfHero.SendMsg, sendMsgListener);
}

let gameWebSocket: GameWebSocket | null = null;
const wsConnectListener: InvokeListener = async (_e, ip, socketPort, token) => {
  if (gameWebSocket) {
    gameWebSocket.close();
  }
  gameWebSocket = new GameWebSocket(ip, socketPort, token, msg => {
    mainWin.webContents.send(IpcChannelsOfHero.ReceiveMsg, msg);
  });
  const connectResult: WsConnectResult = { success: false, err: null };
  try {
    await gameWebSocket.connect();
    connectResult.success = true;
  } catch (err) {
    connectResult.err = err;
  }
  return connectResult;
};

const sendMsgListener: SendListener = (_e, msg) => {
  if (!gameWebSocket) {
    console.error("发送ws消息时，gameWebSocket为空");
    return;
  }
  gameWebSocket.send(msg);
};
