<script setup lang="ts">
import { computed, onMounted, onUnmounted, ref, watch } from "vue";
import { useUpdateDownloadStore } from "@/store/useUpdateDownloadStore.ts";
import IpcChannels from "../../common/IpcChannels.ts";

const updateDownloadStore = useUpdateDownloadStore();

const totalSize = ref(0);
const downloadSize = ref(0);
const errorInfo = ref();
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

function listenDownloadInfo() {
  window.ipc.on(
    IpcChannels.DownloadInfoSyn,
    (event, { downloadBytes: dSize, totalSize: tSize, err }) => {
      if (dSize) {
        downloadSize.value = dSize;
      }
      if (tSize) {
        totalSize.value = tSize;
      }
      if (err) {
        errorInfo.value = err;
      }
    }
  );
}

const emit = defineEmits(["finish"]);

function watchFinish() {
  watch(
    () => {
      if (errorInfo.value) {
        return true;
      }
      return percent.value >= 99.9;
    },
    () => {
      //面板几秒钟后关闭
      setTimeout(() => {
        emit("finish");
        updateDownloadStore.changeInDownload(false);
      }, 10000);
    }
  );
}

onMounted(() => {
  listenDownloadInfo();
  watchFinish();
});

onUnmounted(() => {
  window.ipc.removeAllListeners(IpcChannels.DownloadInfoSyn);
});
</script>

<template>
  <div class="container-DownloadProgress">
    <span class="error-info" v-if="errorInfo">{{ errorInfo }}</span>
    <span v-else class="download-info">
      <span class="linking" v-if="linking">下载连接中...</span>
      <span class="downloading" v-else>
        <span v-if="percent < 99.9">
          下载中
          <span class="progress-bar-container-DownloadProgress">{{
            jinHaoProgress
          }}</span>
          {{ percent }}%
        </span>
        <span v-else>下载完成, 启动安装...</span>
      </span>
    </span>
  </div>
</template>

<style scoped>
.container-DownloadProgress {
  padding: 0 2px;
  border-right: 1px solid lightgray;

  .download-info {
    width: 165px;

    .progress-bar-container-DownloadProgress {
      display: inline-block;
      width: 85px;
      border-left: 1px solid lightgray;
      border-right: 1px solid lightgray;
    }
  }
}
</style>
