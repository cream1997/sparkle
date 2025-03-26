<script setup lang="ts">
import "@xterm/xterm/css/xterm.css";
import { onMounted, onUnmounted, ref } from "vue";
import { Terminal } from "@xterm/xterm";

const terminalRef = ref();

let term: Terminal | null = null;
onMounted(() => {
  term = new Terminal();
  term.open(terminalRef.value);
  term.write("hello world");
});
onUnmounted(() => {
  if (term) {
    term.dispose();
  }
});
</script>

<template>
  <div class="container-cmdPage">
    <div ref="terminalRef"></div>
  </div>
</template>

<style scoped>
.container-cmdPage {
  width: 100%;
  height: 100%;
}
</style>
