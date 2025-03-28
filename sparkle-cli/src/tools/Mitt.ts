import mitt, { type Emitter } from "mitt";

export enum EventKey {
  foo
}

type EventType = {
  [EventKey.foo]: any;
};
/**
 * 组件通信工具
 */
const emitter: Emitter<EventType> = mitt<EventType>();

emitter.on(EventKey.foo, () => {});
emitter.emit(EventKey.foo, {
  a: "aa"
});
export default emitter;
