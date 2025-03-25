<script setup lang="ts">
import IpcChannels from "../common/IpcChannels.ts";
import { onMounted, onUnmounted } from "vue";
import { useRouter } from "vue-router";
import { PagePath } from "@/router/router.ts";

const router = useRouter();

onMounted(() => {
  window.electron.ipcRenderer.on(IpcChannels.ToInitPage, () => {
    router.push(PagePath.Init);
  });
});
onUnmounted(() => {
  window.electron.ipcRenderer.removeAllListeners(IpcChannels.ToInitPage);
});
</script>

<template>
  <router-view></router-view>
</template>

<style scoped></style>
