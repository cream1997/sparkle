<script setup lang="ts">
import IpcChannels from "../../common/IpcChannels.ts";
import { ref } from "vue";
import { useRouter } from "vue-router";
import { PagePath } from "@/router/router.ts";

const router = useRouter();

const btnRef = ref();

async function selectRootDir() {
  btnRef.value.disabled = true;
  let ok = await window.electron.ipcRenderer.invoke(IpcChannels.SelectRootDir);
  if (ok) {
    router.push(PagePath.Home);
  } else {
    // 取消了
    btnRef.value.disabled = false;
  }
}
</script>

<template>
  <div class="init-page-container">
    <button @click="selectRootDir" ref="btnRef">请选择应用根文件夹</button>
  </div>
</template>

<style scoped>
.init-page-container {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

button {
  display: inline-block;
  line-height: 1.6;
}
</style>
