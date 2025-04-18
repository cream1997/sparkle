export interface LoginRes {
  id: string;
  token: string;
}

export interface Role {
  basic: RoleBasic;
}

export interface RoleBasic {
  id: string;
  name: string;
  level: number;
  hp: number;
  mp: number;
  mapId: number;
  x: number;
  y: number;
  createTime: number;
  loginTime: number;
  logoutTime: number;
}

/**
 * 服务器将long都转为了string
 * role里的几个时间要处理一下
 */
export function convertRoleType(role: Role) {
  role.basic.createTime = parseInt(role.basic.createTime as unknown as string);
  role.basic.loginTime = parseInt(role.basic.loginTime as unknown as string);
  role.basic.logoutTime = parseInt(role.basic.logoutTime as unknown as string);
  return role;
}
