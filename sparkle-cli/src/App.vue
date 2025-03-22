<script setup lang="ts">
import IpcChannels from "../common/IpcChannels.ts";
import {onMounted} from "vue";
import {useRouter} from "vue-router";
import {PagePath} from "@/router/router.ts";

const router = useRouter();

onMounted(() => {
  window.electron.ipcRenderer.on(IpcChannels.ToInitPage, () => {
    router.push(PagePath.Init);
  });
});
</script>

<template>
  <router-view></router-view>
</template>

<style scoped>
</style>
