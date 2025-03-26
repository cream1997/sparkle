<script setup lang="ts">
import { type Component, markRaw, reactive, watch } from "vue";
import DownloadProgress from "@/components/DownloadProgress.vue";
import { useUpdateDownloadStore } from "@/store/useUpdateDownloadStore.ts";

const updateDownloadStore = useUpdateDownloadStore();

interface ComponentContent {
  component: Component;
  props?: any;
}

// 定义类型守卫函数
function isComponentContent(it: any): it is ComponentContent {
  return typeof it === "object" && it !== null && "component" in it;
}

const allComponents: (ComponentContent | string)[] = reactive([]);

function pushComponent(it: ComponentContent | string) {
  if (isComponentContent(it)) {
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

function removeItem(item: ComponentContent | string) {
  let index = allComponents.findIndex((it: ComponentContent | string) => {
    return it === item;
  });
  allComponents.splice(index, 1);
}
</script>

<template>
  <div class="container-BottomMainArea">
    <div v-for="(it, index) in allComponents" :key="index">
      <span v-if="isComponentContent(it)">
        <component
          :is="it.component"
          v-bind="it.props"
          @finish="removeItem(it)"
        ></component>
      </span>
      <span v-if="typeof it === 'string'">{{ it }}</span>
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
