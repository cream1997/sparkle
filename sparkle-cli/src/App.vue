<script setup lang="ts">
import IpcChannels, { FloatWinIpcChannels } from "../common/IpcChannels.ts";
import { onMounted, onUnmounted } from "vue";
import { useRouter } from "vue-router";

import { BasePagePath } from "@/router/RouterConst.ts";

const router = useRouter();

onMounted(() => {
  window.ipc.on(IpcChannels.ToInitPage, () => {
    router.push(BasePagePath.Init);
  });

  window.ipc.on(FloatWinIpcChannels.ToFloatWinPage, () => {
    router.push(BasePagePath.FloatWin);
  });
});
onUnmounted(() => {
  window.ipc.removeAllListeners(IpcChannels.ToInitPage);
  window.ipc.removeAllListeners(FloatWinIpcChannels.ToFloatWinPage);
});
</script>

<template>
  <router-view></router-view>
</template>

<style scoped></style>
