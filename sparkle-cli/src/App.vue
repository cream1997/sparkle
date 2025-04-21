<script setup lang="ts">
import IpcChannels from "../common/channels/IpcChannels.ts";
import { onMounted, onUnmounted } from "vue";
import { useRouter } from "vue-router";

import { BasePagePath } from "@/router/RouterConst.ts";
import IpcChannelsOfFloatWin from "../common/channels/IpcChannelsOfFloatWin.ts";

const router = useRouter();

onMounted(() => {
  window.ipc.on(IpcChannels.ToInitPage, () => {
    router.push(BasePagePath.Init);
  });

  window.ipc.on(IpcChannelsOfFloatWin.ToFloatWinPage, () => {
    router.push(BasePagePath.FloatWin);
  });
});
onUnmounted(() => {
  window.ipc.removeAllListeners(IpcChannels.ToInitPage);
  window.ipc.removeAllListeners(IpcChannelsOfFloatWin.ToFloatWinPage);
});
</script>

<template>
  <router-view></router-view>
</template>

<style scoped></style>
