<script setup lang="ts">
import AsideBar from "@/components/layout/AsideBar.vue";
import { onMounted } from "vue";
import IpcChannels from "../../common/IpcChannels.ts";
import { useAppInfoStore } from "@/store/useAppInfoStore.ts";
import BottomInfoPanel from "@/components/layout/bottom/BottomInfoPanel.vue";

const AppInfo = useAppInfoStore();
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
</template>

<style scoped>
.container-home {
  width: 100vw;
  height: 100vh;
  display: flex;
  overflow: hidden;

  aside {
    width: 32px;
    border-right: 1px solid lightgray;
  }

  main {
    width: calc(100% - 32px);
    flex: 1;

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
</style>
