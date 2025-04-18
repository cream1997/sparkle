import { axios } from "../../../../common/net/http/AxiosCfg.ts";
import useAccountStore from "@/store/useAccountStore.ts";

const accountStore = useAccountStore();

export function heroHttpConfig() {
  /**
   * 请求拦截器
   * 不写到axios配置里是因为用到了pinia，axios的配置是主进程、渲染进程共用的，而主进程没有pinia
   */
  axios.interceptors.request.use(
    config => {
      if (config.url?.startsWith("/hero")) {
        if (accountStore.token) {
          config.headers["Authorization"] = `Bearer ${accountStore.token}`;
        }
      }
      return config;
    },
    error => {
      return Promise.reject(error);
    }
  );
}
