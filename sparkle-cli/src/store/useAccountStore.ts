import { defineStore } from "pinia";
import type { Role } from "@/z/hero/types/GameTypes.ts";

const useAccountStore = defineStore("accountStore", {
  state: () => {
    return {
      uid: "",
      token: "",
      allRole: [] as Role[]
    };
  },
  getters: {},
  actions: {
    init(uid: string, token: string, allRole: Role[]) {
      this.uid = uid;
      this.token = token;
      this.allRole = allRole;
    }
  }
});
export default useAccountStore;
