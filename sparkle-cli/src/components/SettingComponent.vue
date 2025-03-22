<script setup lang="ts">
import { markRaw, reactive } from "vue";
import AboutSetting from "@/components/settingSub/AboutSetting.vue";
import MusicSetting from "@/components/settingSub/MusicSetting.vue";
import NoteSetting from "@/components/settingSub/NoteSetting.vue";
import ChatSetting from "@/components/settingSub/ChatSetting.vue";
import ToolSetting from "@/components/settingSub/ToolSetting.vue";

interface Item {
  title: string;
  component: any;
  active?: boolean;
}

const menuItems = reactive<Item[]>([
  {
    title: "关于",
    component: markRaw(AboutSetting),
    active: true
  },
  {
    title: "音乐",
    component: markRaw(MusicSetting)
  },
  {
    title: "笔记",
    component: markRaw(NoteSetting)
  },
  {
    title: "聊天",
    component: markRaw(ChatSetting)
  },
  {
    title: "工具",
    component: markRaw(ToolSetting)
  }
]);

const selectItem = (item: Item) => {
  if (item.active) {
    return;
  }
  menuItems.forEach((item) => {
    delete item.active;
  });
  item.active = true;
};
</script>

<template>
  <div class="container-SettingComponent">
    <aside class="sidebar-SettingComponent">
      <ul class="menu-ul-SettingComponent">
        <li
          v-for="item in menuItems"
          :class="[item.active ? 'li-active-SettingComponent' : '']"
          @click="selectItem(item)"
        >
          <a>{{ item.title }}</a>
        </li>
      </ul>
    </aside>
    <main class="main-SettingComponent">
      <div v-for="item in menuItems">
        <!-- 多一层div是因为下面的v-if无法放到上面,因为我需要给div设置width和height100%,如果没有v-if,无法达到显示效果 -->
        <div v-if="item.active">
          <component :is="item.component" />
        </div>
      </div>
    </main>
  </div>
</template>

<style scoped>
.container-SettingComponent {
  display: flex;
  width: 100%;
  height: 100%;

  .sidebar-SettingComponent {
    width: 150px;
    border-right: 1px solid lightgray;
    padding: 5px 5px;

    .menu-ul-SettingComponent {
      list-style: none;
      margin: 0;
      padding: 0;

      > li {
        text-align: center;
        padding: 3px 2px;
        font-size: 18px;
        cursor: pointer;

        &:hover {
          background-color: #eee;
        }

        &:active {
          background-color: lightgray;
        }

        &.li-active-SettingComponent {
          background-color: skyblue;
        }
      }
    }
  }

  .main-SettingComponent {
    flex: 1;

    > div > div {
      width: 100%;
      height: 100%;
    }
  }
}
</style>
