import { type ComponentPublicInstance, createApp } from "vue";
import "./style.css";
import App from "./App.vue";
import router from "@/router/router.ts";
import { createPinia } from "pinia";
import Tip from "@/tools/Tip.ts";

const app = createApp(App);
globalErrorConfig();
app.use(router).use(createPinia()).mount("#app");

/**
 * 全局错误处理配置
 */
function globalErrorConfig() {
  // 1.捕获vue组件中的错误
  app.config.errorHandler = (error, vm, info) => {
    handleError({ info, error, vm });
  };

  // 2.捕获未处理的Promise拒绝
  window.onunhandledrejection = function (event) {
    handleError({
      info: `reason:${event.reason}, type:unhandledrejection`
    });
  };
  // 3.捕获全局JavaScript错误
  window.onerror = function (message, source, lineno, colno, error) {
    handleError({
      info: `Global error: ${message} ${source} ${lineno} ${colno}}`,
      error
    });
  };
}

/**
 * 统一错误处理入口
 */
function handleError(err: {
  info: string;
  error?: any;
  time?: Date;
  vm?: ComponentPublicInstance | null;
}) {
  err.time = new Date();
  console.error(err);
  Tip.err(err.info);
  // todo 错误记录，比如记录本地日志或者发给服务器
  sendErrToServer(err);
}

// todo
function sendErrToServer(err: object) {}
