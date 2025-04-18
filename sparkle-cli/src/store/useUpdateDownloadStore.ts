import { defineStore } from "pinia";

export const useUpdateDownloadStore = defineStore("updateDownloadStore", {
  state: () => ({
      // 表示是否正在下载中
      inDownload: false
    }),
  actions: {
    changeInDownload(value: boolean) {
      this.inDownload = value;
    }
  }
});
