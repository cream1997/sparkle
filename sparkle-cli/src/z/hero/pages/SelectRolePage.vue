<script setup lang="ts">
import useAccountStore from "@/store/useAccountStore.ts";
import { onMounted, onUnmounted, reactive, ref } from "vue";
import Tip from "@/tools/Tip.ts";
import { postOfHero } from "../../../../common/net/http/AxiosCfg.ts";
import HttpApiOfHero from "../../../../common/net/http/HttpApiOfHero.ts";
import type { Role } from "@/z/hero/types/GameTypes.ts";
import useWatchTokenHook from "@/hooks/useWatchTokenHook.ts";
import msgDispatcher from "@/z/hero/net/ResMsgDispatcher.ts";
import msgSender from "@/z/hero/net/MsgSender.ts";
import IpcChannelsOfHero from "../../../../common/channels/IpcChannelsOfHero.ts";
import { useRouter } from "vue-router";
import { HeroPagePath } from "@/router/RouterConst.ts";

const router = useRouter();

useWatchTokenHook();

const allRole = reactive<Role[]>([]);

const accountStore = useAccountStore();

const createFormShow = ref(false);

const nickName = ref("");

function createRole() {
  if (!nickName.value) {
    Tip.err("昵称不能为空~");
    return;
  }
  postOfHero<Role>(HttpApiOfHero.CreateRole, {
    nickName: nickName.value
  })
    .then(newRole => {
      allRole.push(newRole);
    })
    .catch(err => {
      Tip.err(err);
    });
}

function getAllRole() {
  postOfHero<Role[]>(HttpApiOfHero.GetAllRole).then(roleList => {
    allRole.push(...roleList);
  });
}

function enterRole(role: Role) {
  router.push(HeroPagePath.HeroHome).then(() => {
    /**
     * todo 这里目前需要用BigInt转换一下,因为角色列表和创建角色目前用的是http请求，而http请求目前是将long转为了string;
     * 后续需要将http请求也修改为自定义规则
     */
    msgSender.sendLoginRole(BigInt(role.basic.id));
  });
}

onMounted(() => {
  getAllRole();
  window.ipc.on(IpcChannelsOfHero.ReceiveMsg, (_e, msg) => {
    msgDispatcher.dispatchMsg(msg);
  });
  window.ipc.on(IpcChannelsOfHero.ServerDisconnect, () => {
    // 服务器断开
    accountStore.tokenInvalidDesc = "游戏服务器网络异常，断开连接";
    accountStore.token = "";
  });
});

onUnmounted(() => {
  window.ipc.removeAllListeners(IpcChannelsOfHero.ReceiveMsg);
});
</script>

<template>
  <div class="ctn-selectRolePage">
    <div class="title">
      选择/创建角色
      <button class="logout-btn">退出</button>
    </div>
    <ol class="role-list">
      <li v-for="role in allRole" @click="enterRole(role)">
        {{ role.basic.name }}
      </li>
      <li @click="createFormShow = true">
        <strong class="add-role">+</strong>
      </li>
    </ol>
    <div v-if="createFormShow" class="ctn-createRole">
      <input placeholder="昵称" v-model.trim="nickName" />
      <div>
        <button @click="createRole">创建</button>
        <button @click="createFormShow = false">取消</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.ctn-selectRolePage {
  .title {
    padding: 10px;
    font-size: 20px;
    font-weight: bold;
    text-align: center;
    display: flex;
    justify-content: center;

    .logout-btn {
      position: fixed;
      right: 5px;
    }
  }

  .role-list {
    display: flex;

    > li {
      background-color: lightskyblue;
      width: 150px;
      height: 300px;
      margin-left: 5px;
      display: flex;
      justify-content: center;
      align-items: center;

      &:hover {
        background-color: skyblue;
      }

      &:active {
        background-color: deepskyblue;
      }

      > .add-role {
        font-size: 45px;
      }
    }
  }

  .ctn-createRole {
    margin-top: 20px;
    display: flex;
    flex-direction: column;
    align-items: center;
  }
}
</style>
