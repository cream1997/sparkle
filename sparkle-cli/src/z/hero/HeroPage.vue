<script setup lang="ts">
import { HeroSubPageViewName } from "@/router/RouterConst";
</script>

<template>
  <div id="ctn-heroPage">
    <router-view v-slot="{ Component }" :name="HeroSubPageViewName">
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
