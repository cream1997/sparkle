<script setup lang="ts">
import BottomMainArea from "@/components/layout/bottom/BottomMainArea.vue";
</script>

<template>
  <div class="content-BottomInfoPanel">
    <aside>...</aside>
    <main>
      <BottomMainArea></BottomMainArea>
    </main>
    <aside>...</aside>
  </div>
</template>

<style scoped>
.content-BottomInfoPanel {
  background-color: whitesmoke;
  padding: 1px;
  font-size: 0.8em;
  display: flex;
  width: 100%;

  > aside {
    width: 100px;
    border-right: 1px solid lightgray;
  }

  > main {
    width: calc(100% - 200px);
    flex: 1;
  }

  > aside {
    width: 100px;
    border-left: 1px solid lightgray;
  }
}
</style>
