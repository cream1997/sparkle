<script setup lang="ts">
import useAccountStore from "@/store/useAccountStore.ts";
import { reactive, ref } from "vue";
import Tip from "@/tools/Tip.ts";
import { postOfHero } from "../../../../common/net/http/AxiosCfg.ts";
import HttpApiOfHero from "../../../../common/net/http/HttpApiOfHero.ts";
import type { Role } from "@/z/hero/types/GameTypes.ts";
import useWatchTokenHook from "@/hooks/useWatchTokenHook.ts";

useWatchTokenHook();

const allRole = reactive<Role[]>([]);

const accountStore = useAccountStore();

const createFormShow = ref(false);

const nickName = ref("");

function createRole() {
  if (!nickName.value) {
    Tip.err("昵称不能为空~");
    return;
  }
  postOfHero<Role>(HttpApiOfHero.CreateRole, {
    nickName: nickName.value
  })
    .then(newRole => {
      // accountStore.addRole(newRole);
    })
    .catch(err => {
      Tip.err(err);
    });
}

function enterRole(role: Role) {}
</script>

<template>
  <div class="ctn-selectRolePage">
    <div class="title">
      选择/创建角色
      <button class="logout-btn">退出</button>
    </div>
    <ol class="role-list">
      <li v-for="role in allRole" @click="enterRole(role)">
        {{ role.basic.name }}
      </li>
      <li @click="createFormShow = true">
        <strong class="add-role">+</strong>
      </li>
    </ol>
    <div v-if="createFormShow" class="ctn-createRole">
      <input placeholder="昵称" v-model.trim="nickName" />
      <div>
        <button @click="createRole">创建</button>
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

    .logout-btn {
      position: fixed;
      right: 5px;
    }
  }

  .role-list {
    display: flex;

    > li {
      background-color: lightskyblue;
      width: 150px;
      height: 300px;
      margin-left: 5px;
      display: flex;
      justify-content: center;
      align-items: center;

      &:hover {
        background-color: skyblue;
      }

      &:active {
        background-color: deepskyblue;
      }

      > .add-role {
        font-size: 45px;
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
