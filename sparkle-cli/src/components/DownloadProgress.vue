<script setup lang="ts">
import { computed, ref } from "vue";
import { useUpdateDownloadStore } from "@/store/useUpdateDownloadStore.ts";

const updateDownloadStore = useUpdateDownloadStore();

const totalSize = ref(0);
const downloadSize = ref(0);
const linking = computed(() => {
  return updateDownloadStore.inDownload && totalSize.value === 0;
});

const percent = computed(() => {
  if (totalSize.value === 0) {
    return 0;
  }
  return parseInt(((downloadSize.value / totalSize.value) * 100).toFixed(1));
});

const jinHaoProgress = computed(() => {
  const count = Math.floor(percent.value / 10);
  return "#".repeat(count);
});
</script>

<template>
  <div class="container-DownloadProgress">
    <div v-if="linking">下载连接中...</div>
    <div v-else>
      <div v-if="percent < 99.9">
        下载中
        <span class="progress-bar-container-DownloadProgress">{{
          jinHaoProgress
        }}</span>
        {{ percent }}%
      </div>
      <div v-else>下载完成, 启动安装...</div>
    </div>
  </div>
</template>

<style scoped>
.container-DownloadProgress {
  width: 165px;
  border-right: 1px solid lightgray;

  .progress-bar-container-DownloadProgress {
    display: inline-block;
    width: 85px;
    border-left: 1px solid lightgray;
    border-right: 1px solid lightgray;
  }
}
</style>
