<script setup lang="ts"></script>

<template>
  <div id="ctn-heroPage">
    <router-view v-slot="{ Component }">
      <keep-alive>
        <component :is="Component"></component>
      </keep-alive>
    </router-view>
  </div>
</template>

<style scoped>
#ctn-heroPage {
  width: 100%;
  height: 100%;
}
</style>
