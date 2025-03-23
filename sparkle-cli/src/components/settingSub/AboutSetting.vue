<script setup lang="ts">
import { useAppInfoStore } from "@/store/useAppInfoStore.ts";

const AppInfo = useAppInfoStore();
</script>

<template>
  <div class="container-aboutSetting">
    <div class="outWrapper-aboutSetting">
      <p>
        <span class="label-aboutSetting">程序文件夹</span>
        <input :value="AppInfo.rootDir" disabled />
      </p>
      <p>
        <span class="label-aboutSetting">当前版本：</span>
        {{ AppInfo.version }}
        <button>检查更新</button>
      </p>
    </div>
  </div>
</template>

<style scoped>
.container-aboutSetting {
  width: 100%;
  height: 100%;

  .outWrapper-aboutSetting {
    padding-top: 1.5em;
    width: 80%;
    margin-left: auto;
    margin-right: auto;

    > p {
      padding: 0;
      margin: 0 0 0.5em 0;

      > span {
        display: inline-block;
        width: 100px;
      }
    }
  }
}
</style>
