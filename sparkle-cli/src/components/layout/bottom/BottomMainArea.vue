<script setup lang="ts">
import { type Component, markRaw, reactive, watch } from "vue";
import DownloadProgress from "@/components/DownloadProgress.vue";
import { useUpdateDownloadStore } from "@/store/useUpdateDownloadStore.ts";

const updateDownloadStore = useUpdateDownloadStore();

interface Content {
  component: Component;
  props?: any;
}

// 定义类型守卫函数
function isContent(it: any): it is Content {
  return typeof it === "object" && it !== null && "component" in it;
}

const allComponents: (Content | string)[] = reactive([]);

function pushComponent(it: Content | string) {
  if (isContent(it)) {
    it = markRaw(it);
  }
  allComponents.push(it);
}

watch(
  () => updateDownloadStore.inDownload,
  (value, oldValue, onCleanup) => {
    if (value && !oldValue) {
      pushComponent({
        component: DownloadProgress
      });
    }
  }
);
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
