<template>
  <div class="floating-window">
    <div class="content-area-fw">
      {{ "aaa" }}
    </div>
  </div>
</template>

<script setup lang="ts"></script>

<style scoped>
.floating-window {
  width: 100vw;
  height: 100vh;
  -webkit-app-region: drag;
  background: rgba(255, 255, 255, 0.9);
  cursor: move;
  user-select: none;
  padding: 2px 5px;
}
</style>
