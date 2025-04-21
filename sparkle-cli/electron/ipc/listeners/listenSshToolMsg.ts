import { ipcMain } from "electron";
import IpcChannels from "../../../common/channels/IpcChannels.ts";
import { Client, ClientChannel } from "ssh2";
import { mainWin } from "../../manager/WindowManager.ts";

let client: Client | null;
let clientChannel: ClientChannel | null;
export default function listenSshToolMsg() {
  ipcMain.on(IpcChannels.SshLogin, async (event, param) => {
    if (client) {
      console.error("重复连接");
    } else {
      createSshClient(param);
    }
  });

  ipcMain.on(IpcChannels.SshLogout, async (event, param) => {
    if (client) {
      clientChannel?.close();
      clientChannel = null;
      client.destroy();
      client = null;
    }
  });

  ipcMain.on(IpcChannels.SshSendData, async (event, msg) => {
    // clientChannel.end("ls -l\nexit\n");
    // fixme \n在这里加是否合适？
    clientChannel?.write(msg);
  });
}

function createSshClient(param: any) {
  client = new Client();
  client
    .on("ready", clientOnReady())
    .on("error", err => {
      // 可能是连接错误，比如地址错误，或者账号密码错误
      console.log(err);
    })
    .connect({
      host: param.host,
      port: param.port,
      username: param.username,
      password: param.password
    });
}

function clientOnReady() {
  return () => {
    console.log("ssh客户端准备就绪");
    // 启动交互式shell
    client?.shell((err, stream) => {
      if (err) {
        console.error(err);
        throw err;
      }
      clientChannel = stream;
      clientChannel
        .on("close", () => {
          console.log("stream closed");
          client?.end();
        })
        .on("data", (chunkBuffer: any) => {
          mainWin.webContents.send(IpcChannels.SshReceiveData, chunkBuffer);
        });
    });
  };
}
