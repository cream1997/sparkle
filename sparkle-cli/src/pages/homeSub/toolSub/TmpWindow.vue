<script setup lang="ts">
import { Application } from "pixi.js";
import { onMounted, ref } from "vue";
import "pixi.js/unsafe-eval";

const ctnRef = ref();

onMounted(async () => {
  const app = new Application();
  await app.init({
    background: "#1099bb",
    resizeTo: ctnRef.value
  });
  ctnRef.value.appendChild(app.canvas);
});
</script>

<template>
  <div class="container" ref="ctnRef"></div>
</template>

<style scoped>
.container {
  width: 100%;
  height: 100%;
}
</style>
