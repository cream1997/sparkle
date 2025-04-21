import ReqMsgType from "@/z/hero/net/types/ReqMsgType.ts";
import IpcChannelsOfHero from "../../../../common/channels/IpcChannelsOfHero.ts";

class MsgSender {
  sendLoginRole(rid: number) {
    sendMsg(ReqMsgType.LoginRole, rid);
  }
}

function sendMsg(msgType: ReqMsgType, data?: any) {
  window.ipc.send(IpcChannelsOfHero.SendMsg, { msgType, data });
}

const msgSender = new MsgSender();
export default msgSender;
