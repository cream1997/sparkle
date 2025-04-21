<script setup lang="ts">
import IpcChannels from "../../common/channels/IpcChannels.ts";
import { ref } from "vue";
import { useRouter } from "vue-router";
import WinTitleBar from "@/components/layout/WinTitleBar.vue";
import { BasePagePath } from "@/router/RouterConst.ts";

const router = useRouter();

const btnRef = ref();

async function selectRootDir() {
  btnRef.value.disabled = true;
  let ok = await window.electron.ipcRenderer.invoke(IpcChannels.SelectRootDir);
  if (ok) {
    router.push(BasePagePath.Home);
  } else {
    // 取消了
    btnRef.value.disabled = false;
  }
}
</script>

<template>
  <div class="ctn-initPage">
    <WinTitleBar :showTop="false" />
    <div class="init-page-container">
      <button @click="selectRootDir" ref="btnRef">请选择应用根文件夹</button>
    </div>
  </div>
</template>

<style scoped>
.ctn-initPage {
  width: 100vw;
  height: 100vh;
  display: flex;
  flex-direction: column;

  .init-page-container {
    flex: 1;
    width: 100%;
    height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;

    button {
      display: inline-block;
      line-height: 1.6;
    }
  }
}
</style>
