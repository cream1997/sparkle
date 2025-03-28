<script setup lang="ts">
import "@xterm/xterm/css/xterm.css";
import { nextTick, onMounted, onUnmounted, reactive, ref, toRaw } from "vue";
import { Terminal } from "@xterm/xterm";
import IpcChannels from "../../../common/IpcChannels.ts";
import { FitAddon } from "@xterm/addon-fit";
import Tip from "@/tools/Tip.ts";

const linkInfo = reactive({
  host: "",
  port: "",
  username: "",
  password: ""
});

const terminalContainerRef = ref();
const vimTerminalContainerRef = ref();

const vimTerminalShow = ref(false);
let terminal: Terminal;
let vimTerminal: Terminal | null = null;

function logDecoderStr(chunk: any) {
  // 新增调试输出：将Buffer转换为带转义字符的字符串
  const decoder = new TextDecoder();
  /*    const str = decoder
        .decode(chunk)
        .replace(/\x1B/g, "^[") // 替换ESC字符
        .replace(/\n/g, "\\n") // 显示换行符
        .replace(/\r/g, "\\r"); // 显示回车符*/
  // 通过监听终端输出的特殊转义序列(\x1b[?1049h和\x1b[?1049l)来检测vim等全屏应用的启动和退出
  const str = decoder
    .decode(chunk)
    // eslint-disable-next-line no-control-regex
    .replace(/\u0007/g, "\\a") // BEL (响铃)
    // eslint-disable-next-line no-control-regex
    .replace(/\u0008/g, "\\b") // 退格
    // eslint-disable-next-line no-control-regex
    .replace(/\u0009/g, "\\t") // 水平制表符
    // eslint-disable-next-line no-control-regex
    .replace(/\u001B/g, "\\e") // ESC (建议使用更标准的表示)
    // eslint-disable-next-line no-control-regex
    .replace(/\u000D/g, "\\r") // 回车
    // eslint-disable-next-line no-control-regex
    .replace(/\u000A/g, "\\n") // 换行
    .replace(
      // eslint-disable-next-line no-control-regex
      /[\u0000-\u001F]/g,
      (c) => String.fromCharCode(0x2400 + c.charCodeAt(0)) // 显示为控制图形符号
    );

  console.log("接收到的数据(包含控制序列):", str);
}

function createTerminal(mountEl: HTMLElement) {
  const newTerminal = new Terminal({
    theme: { background: "#1a1a1a", foreground: "#ffffff" },
    fontSize: 16,
    cursorBlink: true,
    cursorStyle: "block"
  });
  const fitAddon = new FitAddon();
  newTerminal.loadAddon(fitAddon);
  newTerminal.open(mountEl);

  function adjustSize() {
    fitAddon.fit();
    // 上面只设置了行列数，但是高度还可能有空隙
    const el: HTMLElement | null = mountEl.querySelector(".xterm-screen");
    if (el) {
      el.style.height = mountEl.offsetHeight + "px";
    }
  }

  adjustSize();
  const resizeObserver = new ResizeObserver(() => {
    adjustSize();
  });
  resizeObserver.observe(mountEl);
  newTerminal.onData((data) => {
    // 直接将所有输入数据发送到SSH服务器，让服务器端处理所有特殊字符
    sendSshMsg(data);
  });
  const originalDispose = newTerminal.dispose.bind(newTerminal);
  newTerminal.dispose = () => {
    resizeObserver.disconnect();
    originalDispose();
  };
  return newTerminal;
}

onMounted(() => {
  terminal = createTerminal(terminalContainerRef.value);
  window.ipc.on(IpcChannels.SshReceiveData, async (event, chunk) => {
    let writeTerminal = terminal;
    let needCloseVimTerminal = false;
    const textDecoder = new TextDecoder();
    if (textDecoder.decode(chunk) === "\u001B[?1h\u001B=") {
      // \e[?1h\e=, 表示开启vim, 可能不够严谨
      vimTerminalShow.value = true;
      await nextTick(() => {
        vimTerminal = createTerminal(vimTerminalContainerRef.value);
        writeTerminal = vimTerminal;
      });
    } else if (textDecoder.decode(chunk).includes("\u001B[?1l\u001B>")) {
      // \e[?1l\e>, 退出vim, 同样可能不严谨
      writeTerminal = vimTerminal!;
      needCloseVimTerminal = true;
    } else if (vimTerminal) {
      writeTerminal = vimTerminal;
    }
    writeTerminal.write(chunk);
    writeTerminal.focus();
    if (needCloseVimTerminal) {
      vimTerminal!.dispose();
      vimTerminal = null;
      vimTerminalShow.value = false;
    }
    // 输出到控制台
    // logDecoderStr(chunk);
  });
});

onUnmounted(() => {
  logout();
  if (terminal) {
    terminal.dispose();
  }
  if (vimTerminal) {
    vimTerminal.dispose();
  }
  window.ipc.removeAllListeners(IpcChannels.SshReceiveData);
});

const login = () => {
  if (
    linkInfo.host &&
    linkInfo.port &&
    linkInfo.username &&
    linkInfo.password
  ) {
    window.ipc.send(IpcChannels.SshLogin, toRaw(linkInfo));
  } else {
    Tip.err("请输入连接信息");
  }
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
      <input placeholder="服务器ip" v-model="linkInfo.host" />
      <input
        placeholder="ssh端口"
        v-model.number="linkInfo.port"
        type="number"
      />
      用户名
      <input v-model="linkInfo.username" />
      密码
      <input v-model="linkInfo.password" type="password" />
      <button @click="login">登录</button>
      <button @click="logout">退出</button>
    </div>
    <div class="content-area">
      <div
        v-show="!vimTerminalShow"
        class="terminal"
        ref="terminalContainerRef"
      ></div>
      <div
        v-if="vimTerminalShow"
        class="terminal"
        ref="vimTerminalContainerRef"
      ></div>
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
