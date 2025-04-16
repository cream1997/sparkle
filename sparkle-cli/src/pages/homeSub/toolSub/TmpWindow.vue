<script setup lang="ts">
import { Application, Assets, Sprite } from "pixi.js";
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
  await Assets.load("hero/monster.png");
  let sprite = Sprite.from("hero/monster.png");
  app.stage.addChild(sprite);
  // Add a variable to count up the seconds our demo has been running
  let elapsed = 0.0;
  // Tell our application's ticker to run a new callback every frame, passing
  // in the amount of time that has passed since the last tick
  app.ticker.add(ticker => {
    // Add the time to our total elapsed time
    elapsed += ticker.deltaTime;
    // Update the sprite's X position based on the cosine of our elapsed time.  We divide
    // by 50 to slow the animation down a bit...
    sprite.x = 100.0 + Math.cos(elapsed / 50.0) * 100.0;
  });
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
