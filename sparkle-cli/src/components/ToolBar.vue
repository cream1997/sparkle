<script setup lang="ts">

const tools = [
  {
    title: "music",
    icon: "music.svg"
  },
  {
    title: "note",
    icon: "note.svg"
  },
  {
    title: "chat",
    icon: "chat.svg"
  },
  {
    title: "setting",
    icon: "setting.svg"
  }
]
const computeIconPath = (item: any) => {
  return new URL(`../assets/icon/${item.icon}`, import.meta.url).href;
}
</script>

<template>
  <div class="all-item-container">
    <div v-for="item in tools" :title="item.title" class="tool-item"
         :class="[item.title==='setting'?'setting-item':'']">
      <img class="item-icon" :src="computeIconPath(item)" :alt="item.title">
    </div>
  </div>
</template>

<style scoped>
.tool-item {
  height: 32px;
  transition: background-color 0.3s ease;
  padding: 2px 0;
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
  display: block;
  width: 25px;
  height: 25px;
  margin: 0 auto;
}
</style>