/// <reference types="vite/client" />
import { ElectronAPI, IpcRenderer } from "@electron-toolkit/preload";

declare global {
  interface Window {
    electron: ElectronAPI;
    ipc: IpcRenderer;
  }
}
