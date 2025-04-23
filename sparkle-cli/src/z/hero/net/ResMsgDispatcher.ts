import type ResMsgType from "@/z/hero/net/types/ResMsgType.ts";

type ResMsgListener = (data: any, msgType: number) => void;
export type ListenerKey = { msgType: ResMsgType; id: number };

class ResMsgDispatcher {
  private readonly msgType2Listeners: Map<
    ResMsgType,
    Map<number, ResMsgListener>
  > = new Map();

  addListener(msgType: ResMsgType, listener: ResMsgListener): ListenerKey {
    let allId2Listener = this.msgType2Listeners.get(msgType);
    if (!allId2Listener) {
      allId2Listener = new Map<number, ResMsgListener>();
      this.msgType2Listeners.set(msgType, allId2Listener);
    }
    // 使用Date.now做id会不会重复？
    const id = Date.now();
    if (allId2Listener.has(id)) {
      // 一般不会重复，因为注册消息监听器不是接受消息，非常不频繁；重复了一定是有问题了
      // 这里也不要重复了，就再简单的调用下Date.now获取，因为不严谨，严谨的话还需要递归检查，直到不重复；这里直接抛异常就可以了
      throw new Error("添加响应消息监听时id重复");
    }
    allId2Listener.set(id, listener);
    return { msgType, id };
  }

  removeListener(key: ListenerKey) {
    const id2Listener = this.msgType2Listeners.get(key.msgType);
    id2Listener!.delete(key.id);
  }

  dispatchMsg(msg: any) {
    const resMsgType = msg.msgType;
    console.log(`收到消息, msgType: ${resMsgType}`);
    const allListener = this.msgType2Listeners.get(resMsgType);
    if (!allListener || allListener.size === 0) {
      throw new Error(`未注册对应类型的监听器 resMsgType: ${resMsgType}`);
    }
    for (const listener of allListener.values()) {
      listener(msg.data, resMsgType);
    }
  }
}

const msgDispatcher = new ResMsgDispatcher();
export default msgDispatcher;
