import axios, { type AxiosInstance, type AxiosRequestConfig } from "axios";
import RetStatus from "./RetStatus.ts";

function createAxiosInstance() {
  const axiosInstance = axios.create({
    timeout: 1000,
    headers: {
      "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"
    }
  });

  // 响应拦截器
  axiosInstance.interceptors.response.use(
    response => {
      const res = response.data;
      switch (res.status) {
        case RetStatus.SUCCESS:
          return response;
        case RetStatus.ERROR:
          return Promise.reject(res.info);
        default:
          throw new Error("错误的状态码");
      }
    },
    error => {
      //处理http状态码错误(如4xx/5xx)
      if (error.response) {
        return Promise.reject(
          new Error(`请求失败, HTTP状态码：${error.response.status}`)
        );
      }
      return Promise.reject(error);
    }
  );

  return axiosInstance;
}

const mainAxiosInstance = createAxiosInstance();
const heroAxiosInstance = createAxiosInstance();

function initAppBaseUrl(serverAddress: string) {
  mainAxiosInstance.defaults.baseURL = serverAddress;
}

function initHeroBaseUrl(heroServerAddress: string) {
  heroAxiosInstance.defaults.baseURL = heroServerAddress;
}

function basePost<T>(
  axiosInstance: AxiosInstance,
  url: string,
  data?: any,
  contentTypeIsJson: boolean = false,
  config?: AxiosRequestConfig
): Promise<T> {
  if (contentTypeIsJson) {
    /**
     * 上面默认设置的是application/x-www-form-urlencoded;charset=UTF-8，
     * 因为大部分请求都是这种格式，后端接口直接就接收了，不需要加@RequestBody
     * 请求发送json格式，就在调用post方法时，将contentTypeIsJson设置为true
     */
    config = config ? config : {};
    config.headers = config.headers ? config.headers : {};
    config.headers["Content-Type"] = "application/json";
  }
  return axiosInstance.post(url, data, config).then(res => res.data.data);
}

function post<T>(
  url: string,
  data?: any,
  contentTypeIsJson: boolean = false,
  config?: AxiosRequestConfig
): Promise<T> {
  return basePost(mainAxiosInstance, url, data, contentTypeIsJson, config);
}

function postOfHero<T>(
  url: string,
  data?: any,
  contentTypeIsJson: boolean = false,
  config?: AxiosRequestConfig
): Promise<T> {
  return basePost(heroAxiosInstance, url, data, contentTypeIsJson, config);
}

export {
  mainAxiosInstance as axios,
  post,
  postOfHero,
  initAppBaseUrl,
  initHeroBaseUrl
};
