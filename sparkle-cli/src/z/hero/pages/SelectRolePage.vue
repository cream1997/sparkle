<script setup lang="ts">
import useAccountStore from "@/store/useAccountStore.ts";
import { ref } from "vue";

const accountStore = useAccountStore();

const createFormShow = ref(false);
</script>

<template>
  <div class="ctn-selectRolePage">
    <div class="title">
      选择/创建角色
      <button class="back-ctn">返回</button>
    </div>
    <ol class="role-list">
      <li v-for="role in accountStore.allRole">{{ role }}</li>
      <li class="add-role" @click="createFormShow = true">
        <strong>+</strong>
      </li>
    </ol>
    <div v-if="createFormShow" class="ctn-createRole">
      <input placeholder="昵称" />
      <div>
        <button>创建</button>
        <button @click="createFormShow = false">取消</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.ctn-selectRolePage {
  .title {
    padding: 10px;
    font-size: 20px;
    font-weight: bold;
    text-align: center;
    display: flex;
    justify-content: center;

    .back-ctn {
      position: fixed;
      right: 5px;
    }
  }

  .role-list {
    > li {
      background-color: lightskyblue;
      width: 150px;
      height: 300px;

      &:hover {
        background-color: skyblue;
      }

      &:active {
        background-color: deepskyblue;
      }

      &.add-role {
        display: flex;
        font-size: 36px;
        justify-content: center;
        align-items: center;
      }
    }
  }

  .ctn-createRole {
    margin-top: 20px;
    display: flex;
    flex-direction: column;
    align-items: center;
  }
}
</style>
