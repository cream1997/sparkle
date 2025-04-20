<script setup lang="ts">
import AsideBar from "@/components/layout/AsideBar.vue";
import { onMounted } from "vue";
import IpcChannels from "../../common/IpcChannels.ts";
import { useAppInfoStore } from "@/store/useAppInfoStore.ts";
import BottomInfoPanel from "@/components/layout/bottom/BottomInfoPanel.vue";
import WinTitleBar from "@/components/layout/WinTitleBar.vue";
import { useRouter } from "vue-router";
import useRouteInfoStore from "@/store/useRouteInfoStore.ts";

import { HeroPageBasePath } from "@/router/RouterConst.ts";

const routeInfoStore = useRouteInfoStore();

const AppInfo = useAppInfoStore();

/**
 * 定义路由守卫用来恢复子路由路径
 */
const router = useRouter();
router.beforeEach((to, from, next) => {
  let skipPath = to.path;
  if (
    !from.path.startsWith(HeroPageBasePath) &&
    to.path.startsWith(HeroPageBasePath)
  ) {
    skipPath = routeInfoStore.heroPageCurrentPath
      ? routeInfoStore.heroPageCurrentPath
      : skipPath;
  }
  if (skipPath.startsWith(HeroPageBasePath)) {
    routeInfoStore.heroPageCurrentPath = skipPath;
  }
  // 不能直接简化写成next(skipPath),因为next函数传值就会导致再执行router.beforeEach导致无限循环
  if (skipPath === to.path) {
    next();
  } else {
    next(skipPath);
  }
});

onMounted(() => {
  window.electron.ipcRenderer
    .invoke(IpcChannels.AskAppInfo)
    .then(({ version, rootDir, serverAddress }) => {
      AppInfo.init(version, rootDir, serverAddress);
    });
});
</script>

<template>
  <div class="container-home">
    <WinTitleBar />
    <div class="mainArea-home">
      <aside>
        <AsideBar></AsideBar>
      </aside>
      <main>
        <div class="main-content-home">
          <router-view v-slot="{ Component }">
            <keep-alive>
              <component :is="Component"></component>
            </keep-alive>
          </router-view>
        </div>
        <div class="bottom-info-panel-home">
          <bottom-info-panel></bottom-info-panel>
        </div>
      </main>
    </div>
  </div>
</template>

<style scoped>
.container-home {
  width: 100vw;
  height: 100vh;
  display: flex;
  flex-direction: column;
  overflow: hidden;

  .mainArea-home {
    flex: 1;
    min-height: 0;
    display: flex;
    overflow: hidden;

    aside {
      width: 32px;
      height: 100%;
      border-right: 1px solid lightgray;
    }

    main {
      flex: 1;
      min-width: 0;

      .main-content-home {
        width: 100%;
        height: calc(100% - 1.25em);
      }

      .bottom-info-panel-home {
        width: 100%;
        border-top: 1px solid lightgray;
        height: 1.25em;
      }
    }
  }
}
</style>
