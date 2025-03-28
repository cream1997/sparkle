<script setup lang="ts">
import { ref } from "vue";
import IpcChannels from "../../../../common/IpcChannels.ts";
import Tip from "@/tools/Tip.ts";

const testCmdResult = ref("");
const sendTestCmd = async () => {
  testCmdResult.value = "waiting...";
  const result = await window.electron.ipcRenderer.invoke(IpcChannels.Test);
  Tip.info(result);
  testCmdResult.value = result;
};
</script>

<template>
  <button @click="sendTestCmd">发送Test命令</button>
  <div>{{ testCmdResult }}</div>
</template>

<style scoped></style>
