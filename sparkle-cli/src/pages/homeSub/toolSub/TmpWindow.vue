<script setup lang="ts">
import { onMounted, ref } from "vue";
import "pixi.js/unsafe-eval";
import { Application, Container, Graphics } from "pixi.js";

const containerRef = ref<HTMLElement>();

// 地图配置
const config = {
  gridSize: 8, // 8x8网格
  tileSize: 64, // 平面瓦片尺寸
  mapWidth: 0, // 动态计算
  mapHeight: 0
};

onMounted(async () => {
  const app = new Application();
  await app.init({
    background: "#1a1a1a",
    resizeTo: containerRef.value,
    antialias: true
  });

  // 创建平面地图
  const flatMap = new Container();
  for (let x = 0; x < config.gridSize; x++) {
    for (let y = 0; y < config.gridSize; y++) {
      // 通道计算
      const red = Math.min(x * 32, 255);
      const green = Math.max(0xff - y * 16, 0);
      const blue = Math.min(y * 32, 255);
      const color = (red << 16) | (green << 8) | blue;
      const tile = new Graphics()
        .rect(0, 0, config.tileSize, config.tileSize)
        .fill({
          color: color, // 动态颜色渐变
          alpha: 0.8
        });
      tile.position.set(x * config.tileSize, y * config.tileSize);
      flatMap.addChild(tile);
    }
  }
  // 计算地图尺寸
  config.mapWidth = config.gridSize * config.tileSize;
  config.mapHeight = config.gridSize * config.tileSize;

  app.stage.addChild(flatMap);
  containerRef.value?.appendChild(app.canvas);
});
</script>

<template>
  <div ref="containerRef" class="map-container"></div>
</template>

<style>
.map-container {
  width: 100%;
  height: 100%;
  background: radial-gradient(circle at center, #2a2a2a 0%, #1a1a1a 100%);
}
</style>
