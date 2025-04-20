import axios, { type AxiosInstance, type AxiosRequestConfig } from "axios";
import RetStatus from "./RetStatus.ts";
import HttpApiOfHero from "./HttpApiOfHero.ts";
import type HttpApi from "./HttpApi.ts";

interface AxiosWithInit extends AxiosInstance {
  _resolveInit: (() => void) | null;
  _queue: Array<() => void>;
}

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
          return res.data;
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

const mainAxiosInstance = createAxiosInstance() as AxiosWithInit;
const heroAxiosInstance = createAxiosInstance() as AxiosWithInit;

//初始化队列和Promise
[mainAxiosInstance, heroAxiosInstance].forEach(instance => {
  new Promise<void>(resolve => {
    instance._resolveInit = resolve;
    instance._queue = [];
  }).then(() => {
    instance._queue?.forEach(fn => fn());
    instance._queue = [];
    instance._resolveInit = null;
  });
});

function initAppBaseUrl(serverAddress: string) {
  mainAxiosInstance.defaults.baseURL = serverAddress;
  mainAxiosInstance._resolveInit?.();
}

function initHeroBaseUrl(heroServerAddress: string) {
  heroAxiosInstance.defaults.baseURL = heroServerAddress;
  heroAxiosInstance?._resolveInit?.();
}

function basePost<T>(
  axiosWithInit: AxiosWithInit,
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
  if (!axiosWithInit._resolveInit) {
    return axiosWithInit.post(url, data, config).then(res => res as T);
  } else {
    return new Promise<T>((resolve, reject) => {
      axiosWithInit._queue?.push(() => {
        axiosWithInit
          .post(url, data, config)
          .then(res => resolve(res as T))
          .catch(reject);
      });
    });
  }
}

function baseGet<T>(
  axiosWithInit: AxiosWithInit,
  url: string,
  params?: any,
  config?: AxiosRequestConfig
): Promise<T> {
  if (params) {
    if (config) {
      config.params = params;
    } else {
      config = {
        params
      };
    }
  }
  if (!axiosWithInit._resolveInit) {
    return axiosWithInit.get(url, config).then(res => res as T);
  } else {
    return new Promise<T>((resolve, reject) => {
      axiosWithInit._queue?.push(() => {
        axiosWithInit
          .get(url, config)
          .then(res => resolve(res as T))
          .catch(reject);
      });
    });
  }
}

function get<T>(
  url: (typeof HttpApi)[keyof typeof HttpApi],
  params?: any,
  config?: AxiosRequestConfig
): Promise<T> {
  return baseGet(mainAxiosInstance, url, params, config);
}

function getOfHero<T>(
  url: (typeof HttpApiOfHero)[keyof typeof HttpApiOfHero],
  params?: any,
  config?: AxiosRequestConfig
): Promise<T> {
  return baseGet(heroAxiosInstance, url, params, config);
}

function post<T>(
  url: (typeof HttpApi)[keyof typeof HttpApi],
  data?: any,
  contentTypeIsJson: boolean = false,
  config?: AxiosRequestConfig
): Promise<T> {
  return basePost(mainAxiosInstance, url, data, contentTypeIsJson, config);
}

function postOfHero<T>(
  url: (typeof HttpApiOfHero)[keyof typeof HttpApiOfHero],
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
  get,
  getOfHero,
  initAppBaseUrl,
  initHeroBaseUrl
};
