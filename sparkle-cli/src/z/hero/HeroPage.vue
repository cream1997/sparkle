<script setup lang="ts">
import { BasePagePath, HeroSubPageViewName } from "@/router/RouterConst";
import useRouteInfoStore from "@/store/useRouteInfoStore.ts";
import { useRouter } from "vue-router";

const router = useRouter();
const routeInfoStore = useRouteInfoStore();

/**
 * 定义路由守卫用来恢复子路由路径
 */
router.beforeEach((to, from, next) => {
  let skipPath = to.path;
  if (
    !from.path.startsWith(BasePagePath.Hero) &&
    to.path.startsWith(BasePagePath.Hero)
  ) {
    skipPath = routeInfoStore.heroPageCurrentPath
      ? routeInfoStore.heroPageCurrentPath
      : skipPath;
  }
  if (skipPath.startsWith(BasePagePath.Hero)) {
    routeInfoStore.heroPageCurrentPath = skipPath;
  }
  // 不能直接简化写成next(skipPath),因为next函数传值就会导致再执行router.beforeEach导致无限循环
  if (skipPath === to.path) {
    next();
  } else {
    next(skipPath);
  }
});
</script>

<template>
  <div id="ctn-heroPage">
    <router-view v-slot="{ Component }" :name="HeroSubPageViewName">
      <keep-alive>
        <component :is="Component"></component>
      </keep-alive>
    </router-view>
  </div>
</template>

<style scoped>
#ctn-heroPage {
  width: 100%;
  height: 100%;
}
</style>
