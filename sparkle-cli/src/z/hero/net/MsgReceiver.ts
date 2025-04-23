import msgDispatcher, {
  type ListenerKey
} from "@/z/hero/net/ResMsgDispatcher.ts";
import type { LoginRoleRes } from "@/z/hero/net/types/ResInterface.ts";
import ResMsgType from "@/z/hero/net/types/ResMsgType.ts";

type MsgHandler<T> = (resData: T, resMsgType: number) => void;

export class MsgReceiver {
  static removeListener(key: ListenerKey) {
    msgDispatcher.removeListener(key);
  }

  onReceiveLoginRole(handler: MsgHandler<LoginRoleRes>) {
    return msgDispatcher.addListener(ResMsgType.LoginRole, handler);
  }
}

const msgReceiver = new MsgReceiver();
export default msgReceiver;
