<script setup lang="ts">
import ToolBar from "@/components/ToolBar.vue";
import { onMounted } from "vue";
import IpcChannels from "../../common/IpcChannels.ts";
import { useAppInfoStore } from "@/store/useAppInfoStore.ts";

const AppInfo = useAppInfoStore();
onMounted(() => {
  window.electron.ipcRenderer
    .invoke(IpcChannels.AskAppInfo)
    .then(({ version, rootDir }) => {
      AppInfo.init(version, rootDir);
    });
});
</script>

<template>
  <div class="container-home">
    <aside>
      <ToolBar></ToolBar>
    </aside>
    <main>
      <router-view></router-view>
    </main>
  </div>
</template>

<style scoped>
.container-home {
  width: 100%;
  height: 100vh;
  display: flex;
}

aside {
  width: 32px;
  border-right: 1px solid lightgray;
}

main {
  flex: 1;
}
</style>
