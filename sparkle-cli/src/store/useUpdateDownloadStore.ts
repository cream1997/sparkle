import { defineStore } from "pinia";

export const useUpdateDownloadStore = defineStore("updateDownloadStore", {
  state: () => {
    return {
      // 表示是否正在下载中
      inDownload: false,
      totalSize: 0,
      downloadSize: 0
    };
  }
});
