import { axios, heroAxios } from "../../../../common/net/http/AxiosCfg.ts";
import useAccountStore from "@/store/useAccountStore.ts";
import type { AxiosInstance } from "axios";

const accountStore = useAccountStore();

export function heroHttpConfig() {
  /**
   * 请求拦截器
   * 不写到axios配置里是因为用到了pinia，axios的配置是主进程、渲染进程共用的，而主进程没有pinia
   */
  requestSetToken(axios);
  requestSetToken(heroAxios);

  function requestSetToken(instance: AxiosInstance) {
    instance.interceptors.request.use(
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
}
