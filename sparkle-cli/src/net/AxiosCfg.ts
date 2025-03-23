import axios, { type AxiosRequestConfig } from "axios";

const axiosInstance = axios.create({
  baseURL: "http://127.0.0.1:8888/",
  timeout: 1000,
  headers: {
    "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"
  }
});

function post<T>(
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
  return axiosInstance.post(url, data, config).then((res) => res.data.data);
}

export { axiosInstance as axios, post };
