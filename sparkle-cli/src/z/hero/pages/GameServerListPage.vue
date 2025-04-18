<script setup lang="ts">
import { computed, onMounted, reactive, ref } from "vue";
import { get } from "../../../../common/net/http/AxiosCfg.ts";
import HttpApi from "../../../../common/net/http/HttpApi.ts";
import type { GameServer } from "@/z/hero/types/GameServerTypes.ts";
import Tip from "@/tools/Tip.ts";
import { IpcChannelsOfHero } from "../../../../common/IpcChannels.ts";
import useAccountStore from "@/store/useAccountStore.ts";

const accountStore = useAccountStore();
const serverList = reactive<GameServer[]>([]);
const inLinkServer = ref("");

const leftList = computed(() => {
  const splitIndex = Math.ceil(serverList.length / 2);
  return serverList.slice(0, splitIndex);
});

const rightList = computed(() => {
  const splitIndex = Math.ceil(serverList.length / 2);
  return serverList.slice(splitIndex);
});

function enterGameServer(server: GameServer) {
  if (inLinkServer.value) {
    Tip.info(`正在连接${inLinkServer.value}...`);
    return;
  }
  // 主进程携带token建立ws连接,连接成功后跳转角色页面
  window.ipc
    .invoke(IpcChannelsOfHero.WsConnect, {
      ip: server.ip,
      socketPort: server.socketPort,
      token: accountStore.token
    })
    .then(res => {
      // todo 成功跳转角色界面
    })
    .catch(err => {
      console.error(err);
      Tip.err(err);
    });

  inLinkServer.value = server.name;
}

onMounted(() => {
  get<GameServer[]>(HttpApi.ServerList).then(res => {
    serverList.push(...res);
  });
});
</script>

<template>
  <div class="ctn-gslp">
    <h3 class="title">服务器列表</h3>
    <div class="server-list">
      <div class="left">
        <ul>
          <li v-for="serverItem in leftList">
            <span @click="enterGameServer(serverItem)">{{
              serverItem.name
            }}</span>
          </li>
        </ul>
      </div>
      <div class="right">
        <ul>
          <li v-for="serverItem in rightList">
            <span @click="enterGameServer(serverItem)">{{
              serverItem.name
            }}</span>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<style scoped>
.ctn-gslp {
  width: 100%;
  height: 100%;

  .title {
    margin-top: 10px;
    text-align: center;
  }

  .server-list {
    display: flex;
    padding: 10px;

    .left,
    .right {
      width: 50%;

      > ul {
        > li {
          height: 36px;
          margin-bottom: 2px;

          > span {
            background-color: whitesmoke;
            text-align: center;
            line-height: 30px;
            display: inline-block;
            width: 100%;

            &:hover {
              background-color: lightgray;
            }

            &:active {
              background-color: gray;
            }
          }
        }
      }
    }

    .left {
      padding-right: 3px;
    }

    .right {
      padding-left: 3px;
    }
  }
}
</style>
