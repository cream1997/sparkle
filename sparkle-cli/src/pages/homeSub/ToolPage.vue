<script setup lang="ts">
import IpcChannels from "../../../common/IpcChannels.ts";
import { ref } from "vue";

const testCmdResult = ref("");
const sendTestCmd = async () => {
  testCmdResult.value = "waiting...";
  const result = await window.electron.ipcRenderer.invoke(IpcChannels.Test);
  console.log("测试命令结果:", result);
  testCmdResult.value = result;
};
</script>

<template>
  <div class="tool-page-container">
    <aside class="tool-page-sidebar">工具边栏</aside>
    <main class="tool-page-main">
      <button @click="sendTestCmd">发送Test命令</button>
      <div>{{ testCmdResult }}</div>
    </main>
  </div>
</template>

<style scoped>
.tool-page-container {
  display: flex;
  width: 100%;
  height: 100%;
}

.tool-page-sidebar {
  width: 150px;
  border-right: 1px solid lightgray;
}

.tool-page-main {
  flex: 1;
}
</style>
