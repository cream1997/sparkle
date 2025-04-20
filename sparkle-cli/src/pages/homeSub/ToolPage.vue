<script setup lang="ts">
import { onActivated, reactive } from "vue";
import { useRouter } from "vue-router";
import { ToolPagePath } from "@/router/RouterConst.ts";

const router = useRouter();
const allTools = reactive([
  { title: "test", path: ToolPagePath.Test, active: true },
  { title: "ipc通信", path: ToolPagePath.IpcTestTool },
  { title: "组件通信", path: ToolPagePath.ComponentTestTool },
  { title: "直播助手", path: ToolPagePath.LiveAssistant },
  { title: "临时窗口", path: ToolPagePath.TmpWindow }
]);

const skip2Page = (it: any) => {
  if (it.active) {
    return;
  }
  const activeItem = allTools.find(it => it.active);
  if (activeItem) {
    delete activeItem.active;
  }
  it.active = true;
  router.push(it.path);
};

onActivated(() => {
  // 调用时机为首次挂载
  // 以及每次从缓存中被重新插入时
  const activeItem = allTools.find(it => it.active);
  if (activeItem) {
    router.push(activeItem.path);
  }
});
</script>

<template>
  <div class="tool-page-container">
    <aside class="tool-page-sidebar">
      <ul>
        <li
          v-for="it in allTools"
          @click="skip2Page(it)"
          :class="[it.active ? 'active' : '']"
        >
          {{ it.title }}
        </li>
      </ul>
    </aside>
    <main class="tool-page-main">
      <router-view v-slot="{ Component }">
        <keep-alive>
          <component :is="Component"></component>
        </keep-alive>
      </router-view>
    </main>
  </div>
</template>

<style scoped>
.tool-page-container {
  display: flex;
  width: 100%;
  height: 100%;

  .tool-page-sidebar {
    width: 150px;
    border-right: 1px solid lightgray;

    > ul {
      list-style-type: none;
      margin: 0;
      padding: 0;

      > li {
        padding: 3px 10px;
        border-bottom: 1px solid navajowhite;
        font-size: 16px;
        text-align: center;

        &:hover {
          background-color: lightskyblue;
        }

        &.active {
          background-color: skyblue;
        }
      }
    }
  }

  .tool-page-main {
    width: calc(100% - 150px);
  }
}
</style>
