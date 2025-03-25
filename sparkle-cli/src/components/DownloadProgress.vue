<script setup lang="ts">
import { computed } from "vue";

interface Props {
  totalSize: number;
  downloadSize: number;
}

const props = defineProps<Props>();

const percent = computed(() =>
  ((props.downloadSize / props.totalSize) * 100).toFixed(1)
);

const jinHaoProgress = computed(() => {
  const count = Math.floor(parseInt(percent.value) / 10);
  return "#".repeat(count);
});
</script>

<template>
  <div class="container-DownloadProgress">
    <div v-if="parseInt(percent) < 100">
      下载中
      <span class="progress-bar-container-DownloadProgress">{{
        jinHaoProgress
      }}</span>
      {{ percent }}%
    </div>
    <div v-else>下载完成, 启动安装...</div>
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
