<script setup lang="ts">
import { ref } from "vue";
import { post } from "@/net/AxiosCfg.ts";
import HttpApiOfHero from "@/hero/net/HttpApiOfHero.ts";

const username = ref("123456");
const password = ref(123456);

function login(event: MouseEvent) {
  event.preventDefault();
  post(HttpApiOfHero.Register, {
    username: username.value,
    password: password.value
  }).then((res) => {});
}

function register(event: MouseEvent) {
  event.preventDefault();
}
</script>

<template>
  <form>
    <div>
      <label for="username">账号</label>
      <input id="username" v-model.number="username" />
    </div>
    <div>
      <label for="password">密码</label>
      <input id="password" type="password" v-model="password" />
    </div>
    <button @click="login">登录</button>
    <button @click="register">注册</button>
  </form>
</template>

<style scoped></style>
