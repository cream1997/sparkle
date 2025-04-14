export interface LoginRes {
  id: string;
  token: string;
  allRole: Role[];
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
