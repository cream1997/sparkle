import { createRouter, createWebHashHistory } from "vue-router";
import {
  BasePageName,
  BasePagePath,
  HeroPageName,
  HeroPagePath,
  HeroSubPageViewName,
  ToolPageName,
  ToolPagePath,
  ToolSubPageViewName
} from "@/router/RouterConst.ts";

const routes = [
  {
    path: "/",
    redirect: BasePagePath.Home
  },
  {
    name: BasePageName.Home,
    path: BasePagePath.Home,
    component: () => import("@/pages/HomePage.vue"),
    children: [
      {
        name: BasePageName.Music,
        path: BasePagePath.Music,
        component: () => import("@/pages/homeSub/MusicPage.vue")
      },
      {
        name: BasePageName.Note,
        path: BasePagePath.Note,
        component: () => import("@/pages/homeSub/NotePage.vue")
      },
      {
        name: BasePageName.Chat,
        path: BasePagePath.Chat,
        component: () => import("@/pages/homeSub/ChatPage.vue")
      },
      {
        name: BasePageName.Cmd,
        path: BasePagePath.Cmd,
        component: () => import("@/pages/homeSub/CmdPage.vue")
      },
      {
        name: BasePageName.Hero,
        path: BasePagePath.Hero,
        component: () => import("@/z/hero/HeroPage.vue"),
        redirect: HeroPagePath.LoginGame,
        children: [
          {
            name: HeroPageName.LoginGame,
            path: HeroPagePath.LoginGame,
            components: {
              [HeroSubPageViewName]: () =>
                import("@/z/hero/pages/LoginGamePage.vue")
            }
          },
          {
            name: HeroPageName.GameServerList,
            path: HeroPagePath.GameServerList,
            components: {
              [HeroSubPageViewName]: () =>
                import("@/z/hero/pages/GameServerListPage.vue")
            }
          },
          {
            name: HeroPageName.SelectRole,
            path: HeroPagePath.SelectRole,
            components: {
              [HeroSubPageViewName]: () =>
                import("@/z/hero/pages/SelectRolePage.vue")
            }
          }
        ]
      },
      {
        name: BasePageName.Tool,
        path: BasePagePath.Tool,
        component: () => import("@/pages/homeSub/ToolPage.vue"),
        children: [
          {
            name: ToolPageName.Test,
            path: ToolPagePath.Test,
            components: {
              [ToolSubPageViewName]: () =>
                import("@/pages/homeSub/toolSub/TestPage.vue")
            }
          },
          {
            name: ToolPageName.IpcTestTool,
            path: ToolPagePath.IpcTestTool,
            components: {
              [ToolSubPageViewName]: () =>
                import("@/pages/homeSub/toolSub/IpcTestPage.vue")
            }
          },
          {
            name: ToolPageName.ComponentTestTool,
            path: ToolPagePath.ComponentTestTool,
            components: {
              [ToolSubPageViewName]: () =>
                import("@/pages/homeSub/toolSub/ComponentTestPage.vue")
            }
          },
          {
            name: ToolPageName.LiveAssistant,
            path: ToolPagePath.LiveAssistant,
            components: {
              [ToolSubPageViewName]: () =>
                import("@/pages/homeSub/toolSub/LiveAssistantPage.vue")
            }
          },
          {
            name: ToolPageName.TmpWindow,
            path: ToolPagePath.TmpWindow,
            components: {
              [ToolSubPageViewName]: () =>
                import("@/pages/homeSub/toolSub/TmpWindow.vue")
            }
          }
        ]
      }
    ]
  },
  {
    name: BasePageName.Init,
    path: BasePagePath.Init,
    component: () => import("@/pages/InitPage.vue")
  },
  {
    name: BasePageName.FloatWin,
    path: BasePagePath.FloatWin,
    component: () => import("@/pages/FloatWinPage.vue")
  }
];

const router = createRouter({
  history: createWebHashHistory(),
  routes
});
export default router;
