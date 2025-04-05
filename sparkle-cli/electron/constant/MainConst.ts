import { app } from "electron";
import * as path from "node:path";

export const BasicCfgName = "sparkle-basic-cfg";
export const AppRootDirKey = "rootDir";
export const ServerAddressKey = "serverAddress";
// 开发时是项目目录，打包后是安装目录
export const AppTmpDir = path.resolve(app.getAppPath(), "appTmp");
// 悬浮窗打开关闭状态改变事件
export const FloatWinVisibleChange = "floatWinVisibleChange";
