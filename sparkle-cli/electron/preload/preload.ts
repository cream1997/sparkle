import { contextBridge } from "electron";
import { electronAPI } from "@electron-toolkit/preload";

// Custom APIs for renderer
const api = {};

// Use `contextBridge` APIs to expose Electron APIs to
// renderer only if context isolation is enabled, otherwise
// just add to the DOM global.
if (process.contextIsolated) {
  try {
    contextBridge.exposeInMainWorld("electron", electronAPI);
    contextBridge.exposeInMainWorld("ipc", electronAPI.ipcRenderer);
    contextBridge.exposeInMainWorld("api", api);
  } catch (error) {
    console.error(error);
  }
  globalErrorHandle();
} else {
  // @ts-expect-error (渲染进程定义了对应的dts)
  window.electron = electronAPI;
  // @ts-expect-error (渲染进程定义了对应的dts)
  window.ipc = electronAPI.ipcRenderer;
  // @ts-expect-error (渲染进程定义了对应的dts)
  window.api = api;
}

/**
 * 如果开启了上下文隔离，要额外注册全局错误处理
 * 上下文隔离contextIsolation会导致导致预加载脚本（preload.js）和渲染进程页面（如 index.html）
 * 运行在不同的JavaScript上下文。IPC的回调函数(如ipcRender.on(xxx,callback)中的callback)在
 * 预加载脚本的上下文中执行，其抛出的错误不会冒泡到渲染进程页面的window.onerror,也就是说会有两个上下文,
 * 两个window
 * 目前阻止默认行为，因为宿主环境的默认打印有时会包含执行上下文(也就是调用栈)
 * onunhandledrejection不需要额外捕获，实测它可以被渲染进程页面捕获(可能因为它是一个事件而不是错误)
 */
function globalErrorHandle() {
  // @ts-expect-error 预加载脚本可以使用window;因为主进程和预加载使用同一份tsconfig，目前简单忽略
  window.onerror = function (
    message: any,
    source?: string,
    lineno?: number,
    colno?: number,
    error?: Error
  ) {
    // todo 目前只是做了简单的打印，后续需要再发送到主进程统一处理(如上报服务器等...)
    console.error(message, source, lineno, colno, error);
  };
}
