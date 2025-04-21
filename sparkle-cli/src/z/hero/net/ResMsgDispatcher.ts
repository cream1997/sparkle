import type ResMsgType from "@/z/hero/net/types/ResMsgType.ts";

type ResMsgListener = (data: any, msgType: number) => void;

class ResMsgDispatcher {
  private readonly msgType2Listeners: Map<ResMsgType, ResMsgListener[]> =
    new Map();

  addListener(msgType: ResMsgType, listener: ResMsgListener) {
    let allListener = this.msgType2Listeners.get(msgType);
    if (!allListener) {
      allListener = [];
      this.msgType2Listeners.set(msgType, allListener);
    }
    allListener.push(listener);
  }

  dispatchMsg(msg: any) {
    const resMsgType = msg.msgType;
    console.log(`收到消息, msgType: ${resMsgType}`);
    const allListener = this.msgType2Listeners.get(resMsgType);
    if (!allListener || allListener.length === 0) {
      throw new Error(`未注册对应类型的监听器 resMsgType: ${resMsgType}`);
    }
    for (const listener of allListener) {
      listener(msg.data, resMsgType);
    }
  }
}

const msgDispatcher = new ResMsgDispatcher();
export default msgDispatcher;
