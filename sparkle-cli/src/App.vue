<script setup lang="ts">
import IpcChannels from "../common/IpcChannels.ts";

const testIpc = () => {
  window.electron.ipcRenderer.send(IpcChannels.Ping, "hello")
}
</script>

<template>
  <button @click="testIpc">test</button>

</template>

<style scoped>
</style>
