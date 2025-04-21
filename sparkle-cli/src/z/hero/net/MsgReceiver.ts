import msgDispatcher from "@/z/hero/net/ResMsgDispatcher.ts";
import ResMsgType from "@/z/hero/net/base/ResMsgType.ts";
import type { LoginRoleRes } from "@/z/hero/net/types/ResInterface.ts";

type MsgHandler<T> = (resData: T, resMsgType: number) => void;

class MsgReceiver {
  onReceiveLoginRole(handler: MsgHandler<LoginRoleRes>) {
    msgDispatcher.addListener(ResMsgType.LoginRole, handler);
  }
}

const msgReceiver = new MsgReceiver();
export default msgReceiver;
