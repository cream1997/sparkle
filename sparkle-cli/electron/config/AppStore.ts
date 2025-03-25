import Store from "electron-store";
import { app } from "electron";
import { AppTmpDir, BasicCfgName, ServerAddressKey } from "../constant/MainConst.ts";
import { is } from "@electron-toolkit/utils";
import AppCfg from "../../common/AppCfg.ts";

/**
 * 这里解释说明一下,首先这个存储应用的一些根本性配置，卸载不删除(方便版本更新不丢失数据)
 * 打包后使用app.getPath("userData"),这个路径形如C:\User\userDir\AppData\Roaming\sparkle-cli(打包配置卸载不删除这个路径的数据)
 * 开发阶段使用app.getAppPath(这个路劲是应用安装路径，开发阶段是项目目录)，不然开发阶段启动应用和安装的应用会冲突(两个都用同一个配置文件)
 */
function getBasicCfgPath(): string {
  if (is.dev) {
    return AppTmpDir;
  } else {
    return app.getPath("userData");
  }
}

const AppCfgStore = new Store({
  cwd: getBasicCfgPath(),
  name: BasicCfgName
});
if (!AppCfgStore.get(ServerAddressKey)) {
  AppCfgStore.set(ServerAddressKey, AppCfg.defaultServer);
}

export { AppCfgStore };
