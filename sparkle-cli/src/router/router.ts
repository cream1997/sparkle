import { createRouter, createWebHashHistory } from "vue-router";

export enum PagePath {
  Home = "/Home",
  Init = "/Init",
  Music = "/Music",
  Note = "/Note",
  Chat = "/Chat",
  Cmd = "/Cmd",
  Hero = "/Hero",
  Tool = "/Tool",
  Test = "/Test",
  IpcTestTool = "/IpcTestTool",
  ComponentTestTool = "/ComponentTestTool",
  LiveAssistant = "/LiveAssistant",
  TmpWindow = "/TmpWindow",
  FloatWin = "/FloatWin"
}

export const HeroPageBasePath = "/Hero";

export const HeroPagePath = {
  LoginGame: `${HeroPageBasePath}/Login`,
  GameServerList: `${HeroPageBasePath}/ServerList`,
  SelectRole: `${HeroPageBasePath}/SelectRole`
} as const;

const HeroPageName = Object.keys(HeroPagePath).reduce(
  (acc, key) => ({ ...acc, [key]: key }),
  {} as { [K in keyof typeof HeroPagePath]: string }
);

const routes = [
  {
    path: "/",
    redirect: PagePath.Home
  },
  {
    path: PagePath.Home,
    component: () => import("@/pages/HomePage.vue"),
    children: [
      {
        path: PagePath.Music,
        component: () => import("@/pages/homeSub/MusicPage.vue")
      },
      {
        path: PagePath.Note,
        component: () => import("@/pages/homeSub/NotePage.vue")
      },
      {
        path: PagePath.Chat,
        component: () => import("@/pages/homeSub/ChatPage.vue")
      },
      {
        path: PagePath.Cmd,
        component: () => import("@/pages/homeSub/CmdPage.vue")
      },
      {
        path: PagePath.Hero,
        component: () => import("@/z/hero/HeroPage.vue"),
        redirect: HeroPagePath.LoginGame,
        children: [
          {
            name: HeroPageName.LoginGame,
            path: HeroPagePath.LoginGame,
            component: () => import("@/z/hero/pages/LoginPage.vue")
          },
          {
            name: HeroPageName.GameServerList,
            path: HeroPagePath.GameServerList,
            component: () => import("@/z/hero/pages/GameServerListPage.vue")
          },
          {
            name: HeroPageName.SelectRole,
            path: HeroPagePath.SelectRole,
            component: () => import("@/z/hero/pages/SelectRolePage.vue")
          }
        ]
      },
      {
        path: PagePath.Tool,
        component: () => import("@/pages/homeSub/ToolPage.vue"),
        children: [
          {
            path: PagePath.Test,
            component: () => import("@/pages/homeSub/toolSub/TestPage.vue")
          },
          {
            path: PagePath.IpcTestTool,
            component: () => import("@/pages/homeSub/toolSub/IpcTestPage.vue")
          },
          {
            path: PagePath.ComponentTestTool,
            component: () =>
              import("@/pages/homeSub/toolSub/ComponentTestPage.vue")
          },
          {
            path: PagePath.LiveAssistant,
            component: () =>
              import("@/pages/homeSub/toolSub/LiveAssistantPage.vue")
          },
          {
            path: PagePath.TmpWindow,
            component: () => import("@/pages/homeSub/toolSub/TmpWindow.vue")
          }
        ]
      }
    ]
  },
  {
    path: PagePath.Init,
    component: () => import("@/pages/InitPage.vue")
  },
  {
    path: PagePath.FloatWin,
    component: () => import("@/pages/FloatWinPage.vue")
  }
];

const router = createRouter({
  history: createWebHashHistory(),
  routes
});
export default router;
