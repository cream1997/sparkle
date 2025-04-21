import type { Role } from "@/z/hero/types/GameTypes.ts";

export interface LoginRoleRes {
  role: Role;
  mapId: number;
  mapName: string;
  width: number;
  height: number;
}
