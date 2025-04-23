<script setup lang="ts">
import useWatchTokenHook from "@/hooks/useWatchTokenHook.ts";
import msgReceiver, { MsgReceiver } from "@/z/hero/net/MsgReceiver.ts";
import type { LoginRoleRes } from "@/z/hero/net/types/ResInterface.ts";
import { onMounted, onUnmounted } from "vue";

useWatchTokenHook();

onMounted(() => {
  const listenerKey = msgReceiver.onReceiveLoginRole((res: LoginRoleRes) => {
    console.log("收到响应");
  });

  onUnmounted(() => {
    // 组件卸载的时候需要移除监听, 不然刷新页面时再进入会注册两次；或者token过期，也会刷新页面，也会导致组件卸载了，但是监听器残留
    MsgReceiver.removeListener(listenerKey);
  });
});
</script>

<template>
  <div>HeroHome</div>
</template>

<style scoped></style>
