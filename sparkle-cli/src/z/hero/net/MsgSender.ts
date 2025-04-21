import ReqMsgType from "@/z/hero/net/base/ReqMsgType.ts";
import { IpcChannelsOfHero } from "../../../../common/IpcChannels.ts";

class MsgSender {
  sendLoginRole(rid: number) {
    sendMsg(ReqMsgType.LoginRole, rid);
  }
}

function sendMsg(type: ReqMsgType, data?: any) {
  window.ipc.send(IpcChannelsOfHero.SendMsg, { type, data });
}

const msgSender = new MsgSender();
export default msgSender;
