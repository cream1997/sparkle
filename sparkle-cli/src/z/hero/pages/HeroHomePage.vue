<script setup lang="ts">
import useWatchTokenHook from "@/hooks/useWatchTokenHook.ts";
import msgReceiver from "@/z/hero/net/MsgReceiver.ts";
import type { LoginRoleRes } from "@/z/hero/net/types/ResInterface.ts";

useWatchTokenHook();

msgReceiver.onReceiveLoginRole((res: LoginRoleRes) => {
  console.log(res);
});
</script>

<template>
  <div>HeroHome</div>
</template>

<style scoped></style>
