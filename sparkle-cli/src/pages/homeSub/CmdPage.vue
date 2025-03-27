<script setup lang="ts">
import "@xterm/xterm/css/xterm.css";
import { onMounted, onUnmounted, ref } from "vue";
import { Terminal } from "@xterm/xterm";
import IpcChannels from "../../../common/IpcChannels.ts";

const host = ref("192.168.33.100");
const port = ref(22);
const username = ref("test");
const password = ref("123456");
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
  let currentLine = "";
  term.onData((data) => {
    if (data === "\x7f" || data === "\x08") {
      // 退格键(Backspace)
      if (currentLine.length > 0) {
        currentLine = currentLine.slice(0, -1);
        // 退格 + 空格 + 退格（覆盖前字符）
        term.write("\b \b");
      }
    } else if (data === "\r") {
      // 处理回车的情况
      term.write("\r\n");
      // 发送当前行
      sendSshMsg(currentLine);
      currentLine = "";
    } else {
      // 其他字符
      currentLine += data;
      term.write(data);
    }
  });

  window.ipc.on(IpcChannels.SshReceiveData, (event, chunk) => {
    term.write(chunk);
  });
});

onUnmounted(() => {
  logout();
  if (term) {
    term.dispose();
  }
  window.ipc.removeAllListeners(IpcChannels.SshReceiveData);
});

const login = () => {
  window.ipc.send(IpcChannels.SshLogin, {
    host: host.value,
    port: port.value,
    username: username.value,
    password: password.value
  });
};
const logout = () => {
  window.ipc.send(IpcChannels.SshLogout);
};

const sendSshMsg = (msg: string) => {
  window.ipc.send(IpcChannels.SshSendData, msg);
};
</script>

<template>
  <div class="container">
    <div class="menu">
      <input disabled placeholder="服务器ip" v-model="host" />
      <input
        disabled
        placeholder="ssh端口"
        v-model.number="port"
        type="number"
      />
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
