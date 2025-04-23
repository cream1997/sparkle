export interface LoginRes {
  id: string;
  token: string;
}

export interface Role {
  basic: RoleBasic;
}

export interface RoleBasic {
  id: bigint;
  uid: bigint;
  name: string;
  level: number;
  hp: number;
  mp: number;
  mapId: number;
  x: number;
  y: number;
  createTime: bigint;
  loginTime: bigint;
  logoutTime: bigint;
}
