export const PagePath = {
  Home: "/Home",
  Init: "/Init",
  Music: "/Music",
  Note: "/Note",
  Chat: "/Chat",
  Cmd: "/Cmd",
  Hero: "/Hero",
  Tool: "/Tool",
  Test: "/Test",
  IpcTestTool: "/IpcTestTool",
  ComponentTestTool: "/ComponentTestTool",
  LiveAssistant: "/LiveAssistant",
  TmpWindow: "/TmpWindow",
  FloatWin: "/FloatWin"
} as const;

export const PageName = Object.keys(PagePath).reduce(
  (acc, key) => ({ ...acc, [key]: key }),
  {} as { [K in keyof typeof PagePath]: string }
);

export const HeroPageBasePath = "/Hero";

export const HeroPagePath = {
  LoginGame: `${HeroPageBasePath}/Login`,
  GameServerList: `${HeroPageBasePath}/ServerList`,
  SelectRole: `${HeroPageBasePath}/SelectRole`
} as const;

export const HeroPageName = Object.keys(HeroPagePath).reduce(
  (acc, key) => ({ ...acc, [key]: key }),
  {} as { [K in keyof typeof HeroPagePath]: string }
);
