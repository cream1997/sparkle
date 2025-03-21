<script setup lang="ts">
const tools = [
  {
    title: "music",
    icon: ""
  },
  {
    title: "note",
    icon: ""
  },
  {
    title: "setting",
    icon: ""
  }
]

</script>

<template>
  <div class="all-item-container">
    <div v-for="item in tools" class="tool-item" :class="[item.title==='setting'?'setting-item':'']">
      {{ item.title }}
    </div>
  </div>
</template>

<style scoped>
.tool-item {
  height: 32px;
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
</style>