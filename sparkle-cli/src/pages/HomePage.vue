<script setup lang="ts">
import ToolBar from "@/components/ToolBar.vue";
import { onMounted } from "vue";
import IpcChannels from "../../common/IpcChannels.ts";
import { useAppInfoStore } from "@/store/useAppInfoStore.ts";
import BottomInfoPanel from "@/components/BottomInfoPanel.vue";

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
      <ToolBar></ToolBar>
    </aside>
    <main>
      <div class="main-content-home">
        <router-view></router-view>
      </div>
      <div class="bottom-info-panel-home">
        <bottom-info-panel></bottom-info-panel>
      </div>
    </main>
  </div>
</template>

<style scoped>
.container-home {
  width: 100%;
  height: 100vh;
  display: flex;

  aside {
    width: 32px;
    border-right: 1px solid lightgray;
  }

  main {
    flex: 1;

    .main-content-home {
      height: calc(100% - 1.25em);
    }

    .bottom-info-panel-home {
      border-top: 1px solid lightgray;
      height: 1.25em;
    }
  }
}
</style>
