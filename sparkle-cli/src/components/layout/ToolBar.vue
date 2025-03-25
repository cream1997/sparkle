<script setup lang="ts">
import { useRouter } from "vue-router";
import { PagePath } from "@/router/router.ts";
import ModalComponent from "@/components/layout/ModalComponent.vue";
import SettingComponent from "@/components/SettingComponent.vue";
import { reactive, ref } from "vue";

const router = useRouter();

interface ToolItem {
  title: string;
  icon: string;
  pagePath?: PagePath;
  active?: boolean;
}

const allTool: ToolItem[] = reactive([
  {
    title: "音乐",
    icon: "music.svg",
    pagePath: PagePath.Music
  },
  {
    title: "笔记",
    icon: "note.svg",
    pagePath: PagePath.Note
  },
  {
    title: "聊天",
    icon: "chat.svg",
    pagePath: PagePath.Chat
  },
  {
    title: "工具",
    icon: "tool.svg",
    pagePath: PagePath.Tool
  }
]);
const settingItem: ToolItem = {
  title: "设置",
  icon: "setting.svg"
};
const computeIconPath = (iconName: string) => {
  return new URL(`../assets/icon/${iconName}`, import.meta.url).href;
};

const selectTool = (item: any) => {
  // todo 检查当前是否已经在此页面
  if (item.active) {
    return;
  }
  allTool.forEach((item) => {
    item.active = false;
  });
  router.push(item.pagePath);
  item.active = true;
};
const modalRef = ref();
const openSettingModal = () => {
  modalRef.value.open();
};
</script>

<template>
  <div class="all-item-container">
    <div
      v-for="item in allTool"
      :title="item.title"
      class="tool-item"
      :class="[item.active ? 'active-tool-item' : '']"
      @click="selectTool(item)"
    >
      <img
        class="item-icon"
        :src="computeIconPath(item.icon)"
        :alt="item.title"
      />
    </div>

    <div
      :title="settingItem.title"
      class="tool-item setting-item"
      @click="openSettingModal"
    >
      <img
        class="item-icon"
        :src="computeIconPath(settingItem.icon)"
        :alt="settingItem.title"
      />
    </div>
  </div>
  <ModalComponent ref="modalRef">
    <SettingComponent></SettingComponent>
  </ModalComponent>
</template>

<style scoped>
.all-item-container {
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: whitesmoke;

  .tool-item {
    height: 32px;
    transition: background-color 0.3s ease;
    padding: 2px 0;
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 1px;

    &:hover {
      background-color: rgba(0, 0, 0, 0.2); /* 背景变深 */
    }

    > .item-icon {
      width: 20px;
      height: 20px;
    }
  }

  .active-tool-item {
    background-color: rgba(0, 0, 0, 0.15);
  }

  .setting-item {
    margin-top: auto;
  }
}
</style>
