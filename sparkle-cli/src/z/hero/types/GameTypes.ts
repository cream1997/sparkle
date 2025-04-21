export interface LoginRes {
  id: string;
  token: string;
}

export interface Role {
  basic: RoleBasic;
}

export interface RoleBasic {
  id: string;
  uid: string;
  name: string;
  level: number;
  hp: number;
  mp: number;
  mapId: number;
  x: number;
  y: number;
  createTime: string;
  loginTime: string;
  logoutTime: string;
}
