import {contextBridge, ipcRenderer} from 'electron'

const ipc = {
    send(channel: string, data: any) {
        ipcRenderer.send(channel, data)
    }
}

// Custom APIs for renderer
const api = {ipc}

// Use `contextBridge` APIs to expose Electron APIs to
// renderer only if context isolation is enabled, otherwise
// just add to the DOM global.
if (process.contextIsolated) {
    try {
        contextBridge.exposeInMainWorld('api', api)
    } catch (error) {
        console.error(error)
    }
} else {
    // @ts-ignore (define in dts)
    window.electron = electronAPI
    // @ts-ignore (define in dts)
    window.api = api
}
