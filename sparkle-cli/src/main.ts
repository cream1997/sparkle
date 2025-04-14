import { type ComponentPublicInstance, createApp } from "vue";
import "./style.css";
import App from "./App.vue";
import router from "@/router/router.ts";
import { createPinia } from "pinia";
import Tip from "@/tools/Tip.ts";

const app = createApp(App);
app.config.errorHandler = handleGlobalError;
window.addEventListener("unhandledrejection", event =>
  handleGlobalError(event.reason, null, "unhandledrejection")
);
app.use(router).use(createPinia()).mount("#app");

/**
 * 捕获组件实例的错误
 * @param err 错误对象
 * @param vm 发生错误的组件实例
 * @param otherInfo vue特定的错误信息，如生命周期钩子名称
 */
function handleGlobalError(
  err: any,
  vm: ComponentPublicInstance | null,
  otherInfo: string
) {
  console.error(err, vm, otherInfo);
  Tip.err(err);
  // todo 错误记录，比如记录本地日志或者发给服务器
}
