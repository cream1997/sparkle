<script setup lang="ts">
import { ref } from "vue";

const content = ref("content");
const test1 = () => {
  content.value = "test1";
};
const test2 = () => {
  content.value = "test2";
};
</script>

<template>
  <div class="container-testPage">
    <p class="title-testPage">Test</p>
    <hr />
    <p class="testAllBtn-testPage">
      <button @click="test1">test1</button>
      <button @click="test2">test2</button>
    </p>
    <hr />
    <div class="testContent-testPage">
      {{ content }}
    </div>
  </div>
</template>

<style scoped>
.container-testPage {
  width: 100%;

  > .title-testPage {
    text-align: center;
    font-size: 20px;
  }

  > .testAllBtn-testPage {
    height: 28px;

    > button {
      display: inline-block;
      height: 26px;
    }
  }

  > .testContent-testPage {
    color: #0f8762;
    font-size: 1.5em;
  }
}
</style>
