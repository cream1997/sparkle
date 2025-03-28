<script setup lang="ts">
import emitter, { EventKey } from "@/tools/Mitt.ts";
import { onMounted, onUnmounted, ref } from "vue";

const content = ref("");

const testEmitter = () => {
  emitter.emit(EventKey.foo, "bar");
};

onMounted(() => {
  emitter.on(EventKey.foo, (arg) => {
    content.value = "";
    setTimeout(() => {
      content.value = arg;
    }, 100);
  });
});

onUnmounted(() => {
  emitter.off(EventKey.foo);
});
</script>

<template>
  <div>
    <p>emitter通信测试</p>
    <p>
      <button @click="testEmitter">test</button>
    </p>

    <div>{{ content }}</div>
  </div>
</template>

<style scoped>
div {
  > p {
    text-align: center;

    > button {
      display: inline-block;
      width: 50px;
      height: 30px;
    }
  }

  > div {
    width: 100%;
    height: 60px;
    border: 1px solid black;
  }
}
</style>
