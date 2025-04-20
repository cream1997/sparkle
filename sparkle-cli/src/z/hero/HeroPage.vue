<script setup lang="ts">
import {
  BasePagePath,
  HeroPagePath,
  HeroSubPageViewName
} from "@/router/RouterConst";
import useRouteInfoStore from "@/store/useRouteInfoStore.ts";
import { useRoute, useRouter } from "vue-router";
import { ref, watchEffect } from "vue";

const router = useRouter();
const route = useRoute();
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
const cacheKey = ref();
watchEffect(() => {
  /**
   * 一旦token失效，比如被值为空，就会跳到登录页,这时让缓存失效
   * todo :key到底是放在router-view标签上还是放在keep-alive标签上(甚至是放在component标签上)暂不清楚，目前的观察是放在两个上都能实现效果
   */
  if (route.path === HeroPagePath.LoginGame) {
    cacheKey.value = Date.now();
  }
});
</script>

<template>
  <div id="ctn-heroPage">
    <router-view v-slot="{ Component }" :name="HeroSubPageViewName">
      <keep-alive :key="cacheKey">
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
