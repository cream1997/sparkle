<script setup lang="ts">
import { type Component, markRaw, onMounted, onUnmounted, reactive } from "vue";
import DownloadProgress from "@/components/DownloadProgress.vue";
import IpcChannels from "../../../../common/IpcChannels.ts";

interface Content {
  component: Component;
  props?: any;
}

// 定义类型守卫函数
function isContent(it: any): it is Content {
  return (
    typeof it === "object" && it !== null && "component" in it && "props" in it
  );
}

const allComponents: (Content | string)[] = reactive([]);

function pushComponent(it: Content | string) {
  if (isContent(it)) {
    it = markRaw(it);
  }
  allComponents.push(it);
}

onMounted(() => {
  window.ipc.on(
    IpcChannels.DownloadInfoSyn,
    (event, downloadBytes: number, totalSize?: number) => {
      if (totalSize) {
        // 开始下载的第一次会发送totalSize
        pushComponent({
          component: DownloadProgress,
          props: {
            totalSize,
            downloadSize: downloadBytes
          }
        });
      } else {
        // 更新数值
      }
    }
  );
});
onUnmounted(() => {
  window.ipc.removeAllListeners(IpcChannels.DownloadInfoSyn);
});
</script>

<template>
  <div class="container-BottomMainArea">
    <div v-for="(it, index) in allComponents" :key="index">
      <component
        v-if="isContent(it)"
        :is="it.component"
        v-bind="it.props"
      ></component>
      <div v-if="typeof it === 'string'">{{ it }}</div>
    </div>
  </div>
</template>

<style scoped>
.container-BottomMainArea {
  width: 100%;
  overflow: hidden;
  display: flex;
}
</style>
