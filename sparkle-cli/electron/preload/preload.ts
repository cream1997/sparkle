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
} else {
  // @ts-expect-error (渲染进程定义了对应的dts)
  window.electron = electronAPI;
  // @ts-expect-error (渲染进程定义了对应的dts)
  window.ipc = electronAPI.ipcRenderer;
  // @ts-expect-error (渲染进程定义了对应的dts)
  window.api = api;
}
