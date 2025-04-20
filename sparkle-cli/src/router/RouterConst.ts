export const BasePagePath = {
  Home: "/Home",
  Init: "/Init",
  Music: "/Music",
  Note: "/Note",
  Chat: "/Chat",
  Cmd: "/Cmd",
  Hero: "/Hero",
  Tool: "/Tool",
  FloatWin: "/FloatWin"
} as const;

export const HeroPagePath = {
  LoginGame: `${BasePagePath.Hero}/Login`,
  GameServerList: `${BasePagePath.Hero}/ServerList`,
  SelectRole: `${BasePagePath.Hero}/SelectRole`
} as const;

export const ToolPagePath = {
  Test: `${BasePagePath.Tool}/Test`,
  IpcTestTool: `${BasePagePath.Tool}/IpcTestTool`,
  ComponentTestTool: `${BasePagePath.Tool}/ComponentTestTool`,
  LiveAssistant: `${BasePagePath.Tool}/LiveAssistant`,
  TmpWindow: `${BasePagePath.Tool}/TmpWindow`
} as const;

export const BasePageName = genePageName(BasePagePath);
export const HeroPageName = genePageName(HeroPagePath);
export const ToolPageName = genePageName(ToolPagePath);

function genePageName<T extends Record<string, string>>(
  pagePathConst: T
): { [K in keyof T]: string } {
  return Object.keys(pagePathConst).reduce(
    (acc, key) => ({ ...acc, [key]: key }),
    {} as { [K in keyof T]: string }
  );
}
