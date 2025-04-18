import { defineStore } from "pinia";

const useAccountStore = defineStore("accountStore", {
  state: () => {
    return {
      uid: "",
      token: ""
    };
  },
  getters: {},
  actions: {
    init(uid: string, token: string) {
      this.uid = uid;
      this.token = token;
    }
  }
});
export default useAccountStore;
