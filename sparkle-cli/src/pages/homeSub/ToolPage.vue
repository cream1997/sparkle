<script setup lang="ts">

</script>

<template>
  <div class="tool-page-container">
    <aside class="tool-page-sidebar">工具边栏</aside>
    <main class="tool-page-main">工具内容区</main>
  </div>
</template>

<style scoped>
.tool-page-container {
  display: flex;
  width: 100%;
  height: 100vh;
}

.tool-page-sidebar {
  width: 150px;
  border-right: 1px solid lightgray;
}

.tool-page-main {
  flex: 1;
}
</style>