// 配置参数
import WebSocket from "ws";

const WsConfig = {
  RECONNECT_INTERVAL: 5000, // 重试间隔（毫秒）
  MAX_RECONNECT_ATTEMPTS: 5, // 最大重试次数
  HEARTBEAT_INTERVAL: 10000 // 心跳间隔
};

type OnReceiveMsg = (msg: any) => void;

export default class GameWebSocket {
  private readonly ip: string;
  private readonly port: number;
  private readonly token: string;
  private readonly _onReceiveMsg: OnReceiveMsg;
  private ws: WebSocket | null = null;
  private reconnectAttempts: number;
  private heartbeatTimer: any;

  constructor(
    ip: string,
    port: number,
    token: string,
    onReceiveMsg: OnReceiveMsg
  ) {
    this.ip = ip;
    this.port = port;
    this.token = token;
    this._onReceiveMsg = onReceiveMsg;
    this.reconnectAttempts = 0;
    this.heartbeatTimer = null;
  }

  // 初始化连接
  connect() {
    this.ws = new WebSocket(
      `ws://${this.ip}:${this.port}/ws?token=${this.token}`,
      {
        // todo 先关闭压缩，因为不确定后端能后处理，待验证修改
        perMessageDeflate: false,
        handshakeTimeout: 5000
      }
    );
    // 事件绑定
    this.ws.on("message", this.handleMessage).on("close", this.handleClose);
    return this.getConnectPromise();
  }

  private getConnectPromise() {
    return new Promise((resolve, reject) => {
      if (this.ws) {
        // fixme 这个error的once监听在成功连接后是否需要移除？
        this.ws.once("error", e => {
          console.error("[WS] Connection error:", e);
          reject(e);
        });
        this.ws.once("open", () => {
          console.log("[WS] Connected to game server");
          // 启动心跳检测
          this.heartbeatTimer = setInterval(() => {
            if (this.ws?.readyState === WebSocket.OPEN) {
              this.ws.ping();
            }
          }, WsConfig.HEARTBEAT_INTERVAL);
          resolve(this);
        });
      }
    });
  }

  // 处理消息
  handleMessage(data: any) {
    try {
      const message = JSON.parse(data);
      this._onReceiveMsg(message);
    } catch (error) {
      console.error("[WS] Message parsing error:", error);
    }
  }

  // 关闭连接
  handleClose(code: any, reason: any) {
    console.log(`[WS] Connection closed: ${code} ${reason}`);
    clearInterval(this.heartbeatTimer);
    // 自动重连逻辑
    /*    if (this.reconnectAttempts < WsConfig.MAX_RECONNECT_ATTEMPTS) {
          setTimeout(() => {
            this.reconnectAttempts++;
            console.log(`[WS] Reconnecting (attempt ${this.reconnectAttempts})...`);
            this.connect();
          }, WsConfig.RECONNECT_INTERVAL * this.reconnectAttempts);
        }*/
  }

  /**
   * 发送消息到服务器
   */
  send(data: any) {
    if (this.ws?.readyState === WebSocket.OPEN) {
      this.ws.send(JSON.stringify(data));
    }
  }

  close() {
    clearInterval(this.heartbeatTimer);
    if (this.ws) {
      this.ws.removeAllListeners();
      this.ws.close();
    }
  }
}
