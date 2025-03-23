import { defineStore } from "pinia";

export const useAppInfoStore = defineStore("appInfoStore", {
  state: () => {
    return {
      version: "",
      rootDir: ""
    };
  },
  getters: {},
  actions: {
    init(version: string, rootDir: string) {
      this.version = version;
      this.rootDir = rootDir;
    }
  }
});
