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
  WinOption = "WinOption"
}

export enum FloatWinIpcChannels {
  ToFloatWinPage = "ToFloatWinPage"
}

export default IpcChannels;
