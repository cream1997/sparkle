<script setup lang="ts">
import { ref } from "vue";
import { post } from "@/net/AxiosCfg.ts";
import HttpApiOfHero from "@/hero/net/HttpApiOfHero.ts";
import swordPng from "@/assets/png/sword.png";
import Tip from "@/tools/Tip.ts";

const username = ref("123456");
const password = ref(123456);

function login(event: MouseEvent) {
  event.preventDefault();
}

function register(event: MouseEvent) {
  event.preventDefault();
  if (!checkForm()) {
    return;
  }
  post(HttpApiOfHero.Register, {
    username: username.value,
    password: password.value
  }).then(res => {});
}

function checkForm(): boolean {
  if (!(username.value && password.value)) {
    Tip.err("有空值");
    return false;
  }
  return true;
}
</script>

<template>
  <div class="ctn-loginPage">
    <h1 class="title">
      <img class="sword-icon" :src="swordPng" alt="勇士之剑" />
      Hero
    </h1>
    <form>
      <div class="row">
        <label for="username">账号</label>
        <input id="username" v-model.number="username" />
      </div>
      <div class="row">
        <label for="password">密码</label>
        <input id="password" type="password" v-model="password" />
      </div>
      <div class="row btn-row">
        <button @click="login">登录</button>
        <button @click="register">注册</button>
      </div>
    </form>
  </div>
</template>

<style scoped>
.ctn-loginPage {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 36px;

  .title {
    .sword-icon {
      height: 25px;
    }
  }

  .row {
    height: 30px;

    > * {
      line-height: 20px;
    }
  }

  .btn-row {
    display: flex;
    justify-content: center;
  }
}
</style>
