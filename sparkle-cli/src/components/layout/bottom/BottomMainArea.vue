<script setup lang="ts">
import { type Component, reactive } from "vue";

interface Content {
  component: Component;
  props: any;
}

const allComponents: (Content | string)[] = reactive([]);

function pushComponent(it: Content | string) {
  allComponents.push(it);
}

// 定义类型守卫函数
function isContent(it: any): it is Content {
  return (
    typeof it === "object" && it !== null && "component" in it && "props" in it
  );
}

function add() {
  pushComponent("abcdefghijklmnopqrst");
}
</script>

<template>
  <div class="container-BottomMainArea">
    <div v-for="(it, index) in allComponents" :key="index">
      <component
        v-if="isContent(it)"
        :is="it.component"
        v-bind="it.props"
      ></component>
      <div v-if="typeof it === 'string'">{{ it }}</div>
    </div>
    <!--    <button @click="add">add</button>-->
  </div>
</template>

<style scoped>
.container-BottomMainArea {
  display: flex;
}
</style>
