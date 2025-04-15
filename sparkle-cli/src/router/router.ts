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
  FloatWin = "/FloatWin"
}

export enum HeroPagePath {
  Login = "/Hero/Login",
  SelectRole = "/Hero/SelectRole"
}

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
        redirect: HeroPagePath.Login,
        children: [
          {
            path: HeroPagePath.Login,
            component: () => import("@/z/hero/pages/LoginPage.vue")
          },
          {
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
