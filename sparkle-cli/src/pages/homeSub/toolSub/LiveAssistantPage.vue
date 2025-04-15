<script setup lang="ts">
import { onMounted, onUnmounted, reactive, ref, toRaw } from "vue";
import Tip from "@/tools/Tip.ts";
import IpcChannels from "../../../../common/IpcChannels.ts";
import Confirm from "@/tools/Confirm.ts";
import { arrToMap, mapToArr } from "@/../common/CommonTools.ts";
import LiveAssistantHeaderComponent from "@/components/tool/live/LiveAssistantHeaderComponent.vue";
import LiveAssistantFooterComponent from "@/components/tool/live/LiveAssistantFooterComponent.vue";

const alreadyConfirm = ref(true);
const pwd = ref("");

interface DataObj {
  allItem: Map<number, Item>;
}

export interface Item {
  id: number;
  name: string;
  game: string;
  type: string;
  createTime: number;
  time: number;
  number: number;
  info: any;
}

const dataObj = reactive<DataObj>({
  allItem: new Map<number, Item>()
});

function save() {
  if (!alreadyConfirm.value) {
    return;
  }
  const raw: any = Object.assign({}, toRaw(dataObj));
  raw.allItem = mapToArr(raw.allItem);
  window.ipc.send(IpcChannels.LiveAssistantSave, raw);
}

function confirm() {
  if (!pwd.value) {
    Tip.err("值无效~");
    return;
  }
  window.ipc
    .invoke(IpcChannels.LiveAssistantGet, pwd.value)
    .then(async (dataStr: string) => {
      if (!dataStr) {
        const ok = await Confirm.show({ msg: "数据为空，是否正常？" });
        if (ok) {
          alreadyConfirm.value = true;
        }
      } else {
        const originObj = JSON.parse(dataStr);
        originObj.allItem = arrToMap(originObj.allItem);
        Object.assign(dataObj, originObj);
        alreadyConfirm.value = true;
      }
    });
}

let intervalNum: any = null;
onMounted(() => {
  intervalNum = setInterval(save, 30000);
});
onUnmounted(() => {
  clearInterval(intervalNum);
});
</script>

<template>
  <div class="ctn-liveAssistant">
    <div class="confirm" v-if="!alreadyConfirm">
      <input type="password" placeholder="密码" v-model.trim="pwd" />
      <button @click="confirm">确认</button>
    </div>
    <div class="content" v-if="alreadyConfirm">
      <header>
        <LiveAssistantHeaderComponent
          :all-item="dataObj.allItem"
        ></LiveAssistantHeaderComponent>
      </header>
      <hr />
      <footer>
        <live-assistant-footer-component
          :all-item="dataObj.allItem"
        ></live-assistant-footer-component>
      </footer>
    </div>
  </div>
</template>

<style scoped>
.ctn-liveAssistant {
  width: 100%;
  height: 100%;

  .content {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;

    > footer {
      flex: 1;
      min-height: 0;
    }
  }
}
</style>
