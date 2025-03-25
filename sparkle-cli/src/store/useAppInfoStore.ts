import { defineStore } from "pinia";
import { initBaseUrl } from "@/net/AxiosCfg.ts";

export const useAppInfoStore = defineStore("appInfoStore", {
  state: () => {
    return {
      version: "",
      rootDir: "",
      serverAddress: ""
    };
  },
  getters: {},
  actions: {
    init(version: string, rootDir: string, serverAddress: string) {
      this.version = version;
      this.rootDir = rootDir;
      this.serverAddress = serverAddress;
      initBaseUrl(serverAddress);
    }
  }
});
