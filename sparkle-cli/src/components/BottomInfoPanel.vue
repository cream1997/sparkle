<script setup lang="ts">
import { ref } from "vue";

const msg = ref("主要内容区");

function showMsg(msg: string): void {}
</script>

<template>
  <div class="content-BottomInfoPanel">
    <aside>左边固定区域</aside>
    <main>{{ msg }}</main>
    <aside>右边固定区域</aside>
  </div>
</template>

<style scoped>
.content-BottomInfoPanel {
  background-color: whitesmoke;
  padding: 1px;
  font-size: 0.8em;
  display: flex;

  > aside {
    width: 100px;
    border-right: 1px solid lightgray;
  }

  > main {
    flex: 1;
  }

  > aside {
    width: 100px;
    border-left: 1px solid lightgray;
  }
}
</style>
