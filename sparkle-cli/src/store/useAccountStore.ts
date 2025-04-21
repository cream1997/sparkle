import { defineStore } from "pinia";

const useAccountStore = defineStore("accountStore", {
  state: () => ({
    // fixme uid目前没有用到，而且后端是通过token解析得到uid的，后续观察是否删除
    uid: "",
    token: "",
    tokenInvalidDesc: ""
  }),
  getters: {},
  actions: {
    init(uid: string, token: string) {
      this.uid = uid;
      this.token = token;
    }
  }
});
export default useAccountStore;
