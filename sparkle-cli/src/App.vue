<script setup lang="ts">
import IpcChannels from "../common/IpcChannels.ts";
import { onMounted, onUnmounted } from "vue";
import { useRouter } from "vue-router";
import { PagePath } from "@/router/router.ts";

const router = useRouter();

onMounted(() => {
  window.ipc.on(IpcChannels.ToInitPage, () => {
    router.push(PagePath.Init);
  });

  window.ipc.on(IpcChannels.ToFloatWinPage, () => {
    router.push(PagePath.FloatWin);
  });
});
onUnmounted(() => {
  window.ipc.removeAllListeners(IpcChannels.ToInitPage);
  window.ipc.removeAllListeners(IpcChannels.ToFloatWinPage);
});
</script>

<template>
  <router-view></router-view>
</template>

<style scoped></style>
