import IpcMainInvokeEvent = Electron.IpcMainInvokeEvent;
import IpcMainEvent = Electron.IpcMainEvent;

export type InvokeListener = (
  event: IpcMainInvokeEvent,
  ...args: any[]
) => Promise<any> | any;

export type SendListener = (event: IpcMainEvent, ...args: any[]) => void;
