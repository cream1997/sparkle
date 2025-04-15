<script setup lang="ts">
import type { Item } from "@/pages/homeSub/toolSub/LiveAssistantPage.vue";

interface Props {
  item: Item;
}

defineProps<Props>();
</script>

<template>
  <div class="ctn-laic">
    <span>{{ item.name }}</span>
    <span>{{ item.game }}</span>
  </div>
</template>

<style scoped>
.ctn-laic {
  background-color: #b8e2f3;
  padding: 2px 10px;
  border-bottom: 1px #55abcf solid;

  > span {
    display: inline-block;
  }
}
</style>
