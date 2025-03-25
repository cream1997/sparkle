<script setup lang="ts">
interface Props {
  totalSize: number;
  downloadSize: number;
}

defineProps<Props>();
</script>

<template>
  <div>总大小{{ totalSize }}， 当前进度{{ downloadSize }}</div>
</template>

<style scoped></style>
