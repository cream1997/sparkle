<script setup lang="ts">
const testIpc = () => {
  window.electron.ipcRenderer.send("ping", "hello")
}
</script>

<template>
  sparkle
  <button @click="testIpc">test</button>
</template>

<style scoped>
</style>
