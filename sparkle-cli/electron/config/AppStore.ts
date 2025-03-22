import Store from "electron-store";
import { app } from "electron";
import { ConfigName } from "../constant/AppConstant";

export const AppCfgStore = new Store({
  cwd: app.getAppPath(),
  name: ConfigName
});
