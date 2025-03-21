import {createRouter, createWebHashHistory} from "vue-router";

const routes = [
    {
        path: "/",
        component: () => import("@/pages/HomePage.vue")
    },
    {
        path: "/init",
        component: () => import("@/pages/InitPage.vue")
    }
]


const router = createRouter({
    history: createWebHashHistory(),
    routes
})
export default router;

