import { createRouter, createWebHashHistory } from "vue-router";
import {
  HeroPageName,
  HeroPagePath,
  PageName,
  PagePath
} from "@/router/RouterConst.ts";

const routes = [
  {
    path: "/",
    redirect: PagePath.Home
  },
  {
    name: PageName.Home,
    path: PagePath.Home,
    component: () => import("@/pages/HomePage.vue"),
    children: [
      {
        name: PageName.Music,
        path: PagePath.Music,
        component: () => import("@/pages/homeSub/MusicPage.vue")
      },
      {
        name: PageName.Note,
        path: PagePath.Note,
        component: () => import("@/pages/homeSub/NotePage.vue")
      },
      {
        name: PageName.Chat,
        path: PagePath.Chat,
        component: () => import("@/pages/homeSub/ChatPage.vue")
      },
      {
        name: PageName.Cmd,
        path: PagePath.Cmd,
        component: () => import("@/pages/homeSub/CmdPage.vue")
      },
      {
        name: PageName.Hero,
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
        name: PageName.Tool,
        path: PagePath.Tool,
        component: () => import("@/pages/homeSub/ToolPage.vue"),
        children: [
          {
            name: PageName.Test,
            path: PagePath.Test,
            component: () => import("@/pages/homeSub/toolSub/TestPage.vue")
          },
          {
            name: PageName.IpcTestTool,
            path: PagePath.IpcTestTool,
            component: () => import("@/pages/homeSub/toolSub/IpcTestPage.vue")
          },
          {
            name: PageName.ComponentTestTool,
            path: PagePath.ComponentTestTool,
            component: () =>
              import("@/pages/homeSub/toolSub/ComponentTestPage.vue")
          },
          {
            name: PageName.LiveAssistant,
            path: PagePath.LiveAssistant,
            component: () =>
              import("@/pages/homeSub/toolSub/LiveAssistantPage.vue")
          },
          {
            name: PageName.TmpWindow,
            path: PagePath.TmpWindow,
            component: () => import("@/pages/homeSub/toolSub/TmpWindow.vue")
          }
        ]
      }
    ]
  },
  {
    name: PageName.Init,
    path: PagePath.Init,
    component: () => import("@/pages/InitPage.vue")
  },
  {
    name: PageName.FloatWin,
    path: PagePath.FloatWin,
    component: () => import("@/pages/FloatWinPage.vue")
  }
];

const router = createRouter({
  history: createWebHashHistory(),
  routes
});
export default router;
