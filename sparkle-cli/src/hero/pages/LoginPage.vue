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
        <input id="username" placeholder="账号" v-model.number="username" />
      </div>
      <div class="row">
        <input
          id="password"
          placeholder="密码"
          type="password"
          v-model="password"
        />
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
  padding-top: 12%;

  .title {
    font-size: 65px;
    margin: 0 0 -20px 0;
    font-family: "Gabriola", serif;
    animation: /*添加淡入动画*/ fadeIn 2s ease-in-out;

    .sword-icon {
      height: 40px;
      /*添加脉冲动画*/
      animation: pulse 0.6s infinite alternate;
    }
  }

  .row {
    height: 35px;

    > input {
      font-size: 16px;
      line-height: 25px;
    }
  }

  .btn-row {
    display: flex;
    justify-content: center;
  }
}

/* 定义淡入动画 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 定义脉冲动画 */
@keyframes pulse {
  from {
    transform: scale(1);
  }
  to {
    transform: scale(1.03);
  }
}
</style>
