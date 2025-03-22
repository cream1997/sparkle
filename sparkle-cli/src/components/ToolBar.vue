<script setup lang="ts">
import {useRouter} from "vue-router";
import {PagePath} from "@/router/router.ts";
import ModalComponent from "@/components/ModalComponent.vue";
import SettingComponent from "@/components/SettingComponent.vue";
import {ref} from "vue";

const router = useRouter();

const tools = [
  {
    title: "music",
    icon: "music.svg",
    pagePath: PagePath.Music
  },
  {
    title: "note",
    icon: "note.svg",
    pagePath: PagePath.Note
  },
  {
    title: "chat",
    icon: "chat.svg",
    pagePath: PagePath.Chat
  },
  {
    title: "tool",
    icon: "tool.svg",
    pagePath: PagePath.Tool
  }
]
const settingItem = {
  title: "Setting",
  icon: "setting.svg",
}
const computeIconPath = (iconName: string) => {
  return new URL(`../assets/icon/${iconName}`, import.meta.url).href;
}

const selectTool = (item: any) => {
  // todo 检查当前是否已经在此页面
  router.push(item.pagePath)
}
const modalRef = ref()
const openSettingModal = () => {
  modalRef.value.open();
}
</script>

<template>
  <div class="all-item-container">
    <div v-for="item in tools" :title="item.title" class="tool-item"
         @click="selectTool(item)">
      <img class="item-icon" :src="computeIconPath(item.icon)" :alt="item.title">
    </div>

    <div :title="settingItem.title" class="tool-item setting-item"
         @click="openSettingModal">
      <img class="item-icon" :src="computeIconPath(settingItem.icon)" :alt="settingItem.title">
    </div>
  </div>
  <ModalComponent ref="modalRef">
    <SettingComponent></SettingComponent>
  </ModalComponent>
</template>

<style scoped>
.tool-item {
  height: 32px;
  transition: background-color 0.3s ease;
  padding: 2px 0;
  display: flex;
  justify-content: center;
  align-items: center;
}

.tool-item:hover {
  background-color: rgba(0, 0, 0, 0.2); /* 背景变深 */
}

.all-item-container {
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.setting-item {
  margin-top: auto;
}

.item-icon {
  width: 20px;
  height: 20px;
}
</style>