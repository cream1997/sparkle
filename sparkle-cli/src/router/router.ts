import {createRouter, createWebHashHistory} from "vue-router";

export enum PagePath {
    Home = "/Home",
    Init = "/Init",
}

const routes = [
    {
        path: "/",
        redirect: PagePath.Home,
    },
    {
        path: PagePath.Home,
        component: () => import("@/pages/HomePage.vue")
    },
    {
        path: PagePath.Init,
        component: () => import("@/pages/InitPage.vue")
    }
]


const router = createRouter({
    history: createWebHashHistory(),
    routes
})
export default router;

