<script setup lang="ts">
import { useAppInfoStore } from "@/store/useAppInfoStore.ts";
import { get } from "../../../common/net/http/AxiosCfg.ts";
import HttpApi from "../../../common/net/http/HttpApi.ts";
import IpcChannels from "../../../common/channels/IpcChannels.ts";
import Tip from "@/tools/Tip.ts";
import Confirm from "@/tools/Confirm.ts";
import { useUpdateDownloadStore } from "@/store/useUpdateDownloadStore.ts";
import { nextTick } from "vue";

const updateDownloadStore = useUpdateDownloadStore();

const AppInfo = useAppInfoStore();

function checkUpdate() {
  // 检查是否有新版本
  get<string>(HttpApi.GetLatestVersionNumber)
    .then(async versionNumber => {
      if (needUpdate(AppInfo.version, versionNumber)) {
        // 确认是否更新
        const confirmed = await Confirm.show({
          msg: `检测到新版本发布,是否下载更新？(v${versionNumber})`
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
    .catch(err => {
      Tip.err("网络异常");
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
  updateDownloadStore.changeInDownload(true);
  nextTick(() => {
    window.ipc.send(IpcChannels.DownloadUpdate, versionNumber);
  });
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
