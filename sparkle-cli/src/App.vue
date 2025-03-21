<script setup lang="ts">
import IpcChannels from "../common/IpcChannels.ts";
import {onMounted} from "vue";


onMounted(() => {
  window.electron.ipcRenderer.on(IpcChannels.SelectRootDir, () => {
    console.log("please select root dir")
  })
})
</script>

<template>

</template>

<style scoped>
</style>
