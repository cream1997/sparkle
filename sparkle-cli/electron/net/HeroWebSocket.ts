// 配置参数
import WebSocket from "ws";

const WsConfig = {
  URL: "ws://127.0.0.1:8889/ws",
  RECONNECT_INTERVAL: 5000, // 重试间隔（毫秒）
  MAX_RECONNECT_ATTEMPTS: 5, // 最大重试次数
  HEARTBEAT_INTERVAL: 30000 // 心跳间隔
};

export default class GameWebSocket {
  set onMsgCallback(value: any) {
    this._onMsgCallback = value;
  }

  private uid: string;
  private token: string;
  private ws: WebSocket;
  private reconnectAttempts: number;
  private heartbeatTimer: any;
  private _onMsgCallback: any;

  constructor(uid: string, token: string) {
    this.uid = uid;
    this.token = token;
    this.reconnectAttempts = 0;
    this.heartbeatTimer = null;
    this.ws = new WebSocket(
      `${WsConfig.URL}?id=${this.uid}&token=${this.token}`,
      {
        perMessageDeflate: false,
        handshakeTimeout: 10000
      }
    );
  }

  // 初始化连接
  connect() {
    // 事件绑定
    this.ws
      .on("message", data => this.handleMessage(data))
      .on("close", (code, reason) => this.handleClose(code, reason));
    return this.getConnectPromise();
  }

  private getConnectPromise() {
    return new Promise((resolve, reject) => {
      this.ws.on("error", error => {
        console.error("[WS] Connection error:", error);
        reject(error);
      });
      this.ws.on("open", () => {
        console.log("[WS] Connected to game server");
        this.reconnectAttempts = 0;
        // 启动心跳检测
        this.heartbeatTimer = setInterval(() => {
          if (this.ws.readyState === WebSocket.OPEN) {
            this.ws.ping();
          }
        }, WsConfig.HEARTBEAT_INTERVAL);
        resolve(this);
      });
    });
  }

  // 处理消息
  handleMessage(data: any) {
    try {
      const message = JSON.parse(data);
      this._onMsgCallback(message);
    } catch (error) {
      console.error("[WS] Message parsing error:", error);
    }
  }

  // 关闭连接
  handleClose(code: any, reason: any) {
    console.log(`[WS] Connection closed: ${code} ${reason}`);
    clearInterval(this.heartbeatTimer);

    // 自动重连逻辑
    if (this.reconnectAttempts < WsConfig.MAX_RECONNECT_ATTEMPTS) {
      setTimeout(() => {
        this.reconnectAttempts++;
        console.log(`[WS] Reconnecting (attempt ${this.reconnectAttempts})...`);
        this.connect();
      }, WsConfig.RECONNECT_INTERVAL * this.reconnectAttempts);
    }
  }

  // 发送消息到服务器
  send(data: any) {
    if (this.ws?.readyState === WebSocket.OPEN) {
      this.ws.send(JSON.stringify(data));
    }
  }

  close() {
    clearInterval(this.heartbeatTimer);
    this.ws.removeAllListeners();
    this.ws.close();
  }
}
