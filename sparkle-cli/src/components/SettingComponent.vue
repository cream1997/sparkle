<script setup lang="ts">

</script>

<template>
  <div class="setting-page-container">
    <aside class="setting-page-sidebar">边栏</aside>
    <main class="setting-page-main">内容区</main>
  </div>
</template>

<style scoped>
.setting-page-container {
  display: flex;
  width: 100%;
  height: 100%;
}

.setting-page-sidebar {
  width: 150px;
  border-right: 1px solid lightgray;
}

.setting-page-main {
  flex: 1;
}
</style>