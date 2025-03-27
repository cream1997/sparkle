<script setup lang="ts">
import "@xterm/xterm/css/xterm.css";
import { onMounted, onUnmounted, reactive, ref, toRaw } from "vue";
import { Terminal } from "@xterm/xterm";
import IpcChannels from "../../../common/IpcChannels.ts";
import { FitAddon } from "@xterm/addon-fit";

const linkInfo = reactive({
  host: "192.168.33.100",
  port: 22,
  username: "test",
  password: "123456"
});

const terminalContainerRef = ref();
let terminal: Terminal;
onMounted(() => {
  terminal = new Terminal({
    theme: { background: "#1a1a1a", foreground: "#ffffff" },
    fontSize: 16,
    cursorBlink: true,
    cursorStyle: "bar"
  });
  const fitAddon = new FitAddon();
  terminal.loadAddon(fitAddon);
  terminal.open(terminalContainerRef.value);

  function adjustSize() {
    fitAddon.fit();
    // 上面只设置了行列数，但是高度还可能有空隙
    const el = terminalContainerRef.value.querySelector(".xterm-screen");
    if (el) {
      el.style.height = terminalContainerRef.value.offsetHeight + "px";
    }
  }

  adjustSize();
  const resizeObserver = new ResizeObserver(() => {
    adjustSize();
  });
  resizeObserver.observe(terminalContainerRef.value);
  onUnmounted(() => {
    resizeObserver.disconnect();
  });
  let currentLine = "";
  terminal.onData((data) => {
    if (data === "\x7f" || data === "\x08") {
      // 退格键(Backspace)
      if (currentLine.length > 0) {
        currentLine = currentLine.slice(0, -1);
        // 退格 + 空格 + 退格（覆盖前字符）
        terminal.write("\b \b");
      }
    } else if (data === "\r") {
      // 处理回车的情况
      terminal.write("\r\n");
      // 发送当前行
      sendSshMsg(currentLine);
      currentLine = "";
    } else {
      // 其他字符
      currentLine += data;
      terminal.write(data);
    }
  });

  window.ipc.on(IpcChannels.SshReceiveData, (event, chunk) => {
    terminal.write(chunk);
  });
});

onUnmounted(() => {
  logout();
  if (terminal) {
    terminal.dispose();
  }
  window.ipc.removeAllListeners(IpcChannels.SshReceiveData);
});

const login = () => {
  window.ipc.send(IpcChannels.SshLogin, toRaw(linkInfo));
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
      <input disabled placeholder="服务器ip" v-model="linkInfo.host" />
      <input
        disabled
        placeholder="ssh端口"
        v-model.number="linkInfo.port"
        type="number"
      />
      用户名
      <input disabled v-model="linkInfo.username" />
      密码
      <input v-model="linkInfo.password" type="password" />
      <button @click="login">登录</button>
      <button @click="logout">退出</button>
    </div>
    <div class="content-area">
      <div class="terminal" ref="terminalContainerRef"></div>
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

    > input {
      width: 100%;
    }
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
