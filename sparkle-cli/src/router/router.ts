import { createRouter, createWebHashHistory } from "vue-router";

export enum PagePath {
  Home = "/Home",
  Init = "/Init",
  Music = "/Music",
  Note = "/Note",
  Chat = "/Chat",
  Cmd = "/Cmd",
  Tool = "/Tool"
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
        path: PagePath.Tool,
        component: () => import("@/pages/homeSub/ToolPage.vue")
      }
    ]
  },
  {
    path: PagePath.Init,
    component: () => import("@/pages/InitPage.vue")
  }
];

const router = createRouter({
  history: createWebHashHistory(),
  routes
});
export default router;
