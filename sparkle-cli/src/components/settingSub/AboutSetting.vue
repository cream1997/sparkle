<script setup lang="ts">
import { useAppInfoStore } from "@/store/useAppInfoStore.ts";
import { axios } from "@/net/AxiosCfg.ts";
import NetApi from "../../../common/NetApi.ts";
import IpcChannels from "../../../common/IpcChannels.ts";
import Tip from "@/tools/Tip.ts";
import Confirm from "@/tools/Confirm.ts";
import { useUpdateDownloadStore } from "@/store/useUpdateDownloadStore.ts";

const updateDownloadStore = useUpdateDownloadStore();

const AppInfo = useAppInfoStore();

function checkUpdate() {
  // 检查是否有新版本
  axios
    .get(NetApi.GetLatestVersionNumber)
    .then(async ({ data: { data: versionNumber } }) => {
      if (needUpdate(AppInfo.version, versionNumber)) {
        // 确认是否更新
        const confirmed = await Confirm.show({
          msg: `是否下载新版本？(v${versionNumber})`
        });
        if (!confirmed) {
          return;
        }
        if (updateDownloadStore.inDownload) {
          Tip.err("正在下载中~");
        } else {
          downloadUpdate(versionNumber);
        }
      } else {
        Tip.info("已是最新版本");
      }
    })
    .catch((err) => {
      Tip.err(err);
    });
}

function needUpdate(currentVersion: string, latestVersion: string) {
  if (!currentVersion) {
    // 当前版本为空，说明应用有些异常
    throw new Error("应用程序检测当前版本异常");
  }
  // 直接比较是否相等，而不是大小，简单但不严谨
  return currentVersion != latestVersion;
}

function downloadUpdate(versionNumber: string) {
  updateDownloadStore.inDownload = true;
  window.ipc.send(IpcChannels.DownloadUpdate, versionNumber);
}
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
        v{{ AppInfo.version }}
        <button @click="checkUpdate">检查更新</button>
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
