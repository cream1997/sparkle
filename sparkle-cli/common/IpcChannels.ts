enum IpcChannels {
  Test = "Test",
  ToInitPage = "ToInitPage",
  SelectRootDir = "SelectRootDir",
  AskAppInfo = "AskAppInfo",
  DownloadUpdate = "DownloadUpdate",
  DownloadInfoSyn = "DownloadInfoSyn",
  SshLogin = "SshLogin",
  SshLogout = "SshLogout",
  SshReceiveData = "SshReceiveData",
  SshSendData = "SshSendData",
  WinOption = "WinOption",
  LiveAssistantGet = "LiveAssistantGet",
  LiveAssistantSave = "LiveAssistantSave"
}

export enum FloatWinIpcChannels {
  ToFloatWinPage = "ToFloatWinPage"
}

export enum IpcChannelsOfHero {
  WsConnect = "WsConnect"
}

export default IpcChannels;
