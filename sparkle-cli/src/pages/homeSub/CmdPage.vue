<script setup lang="ts">
import "@xterm/xterm/css/xterm.css";
import { onMounted, onUnmounted, ref } from "vue";
import { Terminal } from "@xterm/xterm";
import IpcChannels from "../../../common/IpcChannels.ts";

const serverIp = ref("192.168.33.100");
const port = ref(22);
const username = ref("root");
const password = ref("");
const terminalRef = ref();

let term: Terminal;
onMounted(() => {
  term = new Terminal({
    rows: 30,
    theme: { background: "#1a1a1a", foreground: "#ffffff" },
    fontSize: 16,
    cursorBlink: true,
    cursorStyle: "bar"
  });
  term.open(terminalRef.value);
  term.write("hello world");
  term.onData((data) => {
    if (data === "\x7f" || data === "\x08") {
      // 退格键(Backspace)
      term.write("\b \b"); // 退格 + 空格 + 退格（覆盖前字符）
    } else if (data === "\r") {
      term.write("\r\n");
    } else {
      // 其他字符
      term.write(data);
    }
  });
});

onUnmounted(() => {
  if (term) {
    term.dispose();
  }
});

const login = () => {
  window.ipc.send(IpcChannels.SshLogin, { serverIp, port, username, password });
};
const logout = () => {
  window.ipc.send(IpcChannels.SshLogout);
};
</script>

<template>
  <div class="container">
    <div class="menu">
      <input disabled placeholder="服务器ip" v-model="serverIp" />
      <input disabled placeholder="ssh端口" v-model="port" />
      用户名
      <input disabled v-model="username" style="width: 100%" />
      密码
      <input v-model="password" type="password" style="width: 100%" />
      <button @click="login">登录</button>
      <button @click="logout">退出</button>
    </div>
    <div class="content-area">
      <div class="terminal" ref="terminalRef"></div>
      <div class="terminal-bottom-info-panel">终端信息栏</div>
    </div>
  </div>
</template>

<style scoped>
.container {
  width: 100%;
  height: 100%;
  display: flex;

  .menu {
    width: 150px;
    border-right: 2px solid lightgray;
  }

  .content-area {
    width: calc(100% - 150px);

    .terminal-bottom-info-panel {
      width: 100%;
      height: 20px;
      border-top: 1px solid skyblue;
    }

    .terminal {
      width: 100%;
      height: calc(100% - 20px);
    }
  }
}
</style>
