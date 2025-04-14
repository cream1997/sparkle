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
      allRole.forEach(role => convertType(role));
      this.allRole = allRole;
    },
    addRole(role: Role) {
      this.allRole.push(convertType(role));
    }
  }
});
export default useAccountStore;

/**
 * 服务器将long都转为了string
 * role里的几个时间要处理一下
 */
function convertType(role: Role) {
  role.basic.createTime = parseInt(role.basic.createTime as unknown as string);
  role.basic.loginTime = parseInt(role.basic.loginTime as unknown as string);
  role.basic.logoutTime = parseInt(role.basic.logoutTime as unknown as string);
  return role;
}
