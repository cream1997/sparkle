<script setup lang="ts">
import type { Item } from "@/pages/homeSub/toolSub/LiveAssistantPage.vue";

interface Props {
  allItem: Map<number, Item>;
}

defineProps<Props>();
</script>

<template>
  <div>footer</div>
</template>

<style scoped></style>
