<script setup lang="ts">
import {ref} from "vue";

interface Props {
  top?: string;
  bottom?: string;
  left?: string;
  right?: string;
}

const props = withDefaults(defineProps<Props>(), {
  top: "3em",
  bottom: "3em",
  right: "15%",
  left: "15%"
});
const show = ref(false);
const modalRef = ref();

const closeModal = () => {
  show.value = false;
};
const open = () => {
  show.value = true;
};
defineExpose({
  open
});
</script>

<template>
  <div v-if="show" ref="modalRef">
    <div class="backdrop-ModalCom"></div>
    <div class="body--ModalCom" :style="{
        top,
        bottom,
        right,
        left,
    }">
      <button class="close-ModalCom" @click="closeModal">&times;</button>
      <slot></slot>
    </div>
  </div>
</template>

<style scoped>
.backdrop-ModalCom {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background-color: rgba(0, 0, 0, 0.5);
}

.body--ModalCom {
  font-family: Arial, Helvetica, sans-serif;
  position: fixed;
  background-color: white;
  border: 1px solid black;
  border-radius: 6px;
}

.close-ModalCom {
  display: block;
  width: 25px;
  height: 25px;
  line-height: 25px;
  position: absolute;
  top: 0.1em;
  right: 0.1em;
  font-size: 35px;
  padding: 0;
  cursor: pointer;
  border: none;
  border-radius: 3px;
  background-color: transparent;
}

.close-ModalCom:hover {
  background-color: rgba(0, 0, 0, 0.2);
}
</style>