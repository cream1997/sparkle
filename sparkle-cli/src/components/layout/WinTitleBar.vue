<script setup lang="ts">
import icon from "/icon.svg";
import topSvg from "@/assets/icon/top.svg";
import closeSvg from "@/assets/icon/close.svg";
import maxSvg from "@/assets/icon/max.svg";
import minSvg from "@/assets/icon/min.svg";
import unTopSvg from "@/assets/icon/unTop.svg";
import restoreSvg from "@/assets/icon/restore.svg";
import { ref } from "vue";
import IpcChannels from "../../../common/channels/IpcChannels.ts";
import { WinOpType } from "../../../common/constants/CommonConst.ts";

defineProps({
  showTop: {
    type: Boolean,
    default: true
  }
});

const isTop = ref(false);
const isMax = ref(false);

const winOp = (type: WinOpType) => {
  window.ipc.send(IpcChannels.WinOption, type);
};

const close = () => {
  winOp(WinOpType.Close);
};
const maximize = () => {
  if (isMax.value) {
    winOp(WinOpType.Restore);
  } else {
    winOp(WinOpType.Maximize);
  }
  isMax.value = !isMax.value;
};
const minimum = () => {
  winOp(WinOpType.Minimum);
};
const top = () => {
  if (isTop.value) {
    winOp(WinOpType.UnTop);
  } else {
    winOp(WinOpType.Top);
  }
  isTop.value = !isTop.value;
};
</script>

<template>
  <div class="container-winTitleBar">
    <img :src="icon" alt="Sparkle" class="sparkle-icon-WTB" />
    <div class="app-name-WTB">Sparkle</div>
    <div class="menu-WTB">菜单</div>
    <div class="winOption-WTB">
      <div v-if="showTop" :title="isTop ? '取消置顶' : '置顶'" @click="top">
        <img :src="isTop ? unTopSvg : topSvg" alt="置顶/取消置顶" />
      </div>
      <div title="最小化" @click="minimum">
        <img :src="minSvg" alt="最小化" />
      </div>
      <div :title="isMax ? '恢复' : '最大化'" @click="maximize">
        <img :src="isMax ? restoreSvg : maxSvg" alt="最大化/恢复" />
      </div>
      <div title="关闭" @click="close">
        <img :src="closeSvg" alt="关闭" />
      </div>
    </div>
  </div>
</template>

<style scoped>
.container-winTitleBar {
  /*添加可拖拽区域*/
  -webkit-app-region: drag;
  width: 100vw;
  height: 28px;
  /* 背景改为带透明度的浅青绿色 */
  /* 添加微质感纹理 */
  background: rgba(220, 242, 235, 0.9)
    radial-gradient(
      circle at 50% 50%,
      rgba(255, 255, 255, 0.2) 10%,
      transparent 50%
    );
  display: flex;
  align-items: center;

  .sparkle-icon-WTB {
    height: 16px;
    margin: 0 8px;
  }

  .app-name-WTB {
    font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
    font-size: 12px;
  }

  .menu-WTB {
    padding: 0 6px;
    font-size: 14px;
  }

  .winOption-WTB {
    -webkit-app-region: no-drag;
    height: 100%;
    margin-left: auto;
    display: flex;

    > div {
      padding: 6px 10px 0 10px;

      &:hover {
        background-color: rgb(209, 230, 178);
      }

      &:last-child:hover {
        background-color: rgb(196, 43, 28);
      }

      &:active {
        background-color: rgb(156, 180, 128);
      }

      &:last-child:active {
        background-color: rgb(136, 33, 18);
      }

      > img {
        height: 15px;
      }
    }
  }
}
</style>
