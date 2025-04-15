<script setup lang="ts">
import type { Item } from "@/pages/homeSub/toolSub/LiveAssistantPage.vue";
import { computed, reactive, ref, toRaw } from "vue";
import Tip from "@/tools/Tip.ts";
import Times from "@/tools/Times.ts";
import Confirm from "@/tools/Confirm.ts";
import LiveAssistantItemComponent from "@/components/tool/live/LiveAssistantItemComponent.vue";

interface Props {
  allItem: Map<number, Item>;
}

const props = defineProps<Props>();

const formItem = reactive<Item>({
  id: 0,
  name: "",
  game: "",
  type: "",
  createTime: 0,
  time: 0,
  number: 0,
  info: null
});

async function formSubmit() {
  if (formItem.name && formItem.game && formItem.type) {
    formItem.id = Times.now();
    formItem.createTime = Times.now();
    // 检查重复
    if (mayRepeat(formItem)) {
      const ok = await Confirm.show({ msg: "可能重复，是否继续添加?" });
      if (!ok) {
        return;
      }
    }
    props.allItem.set(formItem.id, Object.assign({}, toRaw(formItem)));
    nameLikeOrId.value = formItem.id;
    // reset
    formItem.id = 0;
    formItem.name = "";
    formItem.createTime = 0;
  } else {
    Tip.err("有空值");
  }
}

function mayRepeat(item: Item) {
  for (let it of props.allItem.values()) {
    if (it.name.includes(item.name) || item.name.includes(it.name)) {
      return true;
    }
  }
  return false;
}

const nameLikeOrId = ref();

const filterList = computed(() => {
  const res: Item[] = [];
  if (!nameLikeOrId.value) {
    return res;
  }
  const val = props.allItem.get(parseInt(nameLikeOrId.value));
  if (val) {
    res.push(val);
  } else {
    for (let it of props.allItem.values()) {
      if (it.name.toLowerCase().includes(nameLikeOrId.value.toLowerCase())) {
        res.push(it);
      }
    }
  }
  return res;
});

const item = ref<Item | null>(null);

function clickItem(item: Item) {}
</script>

<template>
  <div class="ctn-lahc">
    <div class="top-lahc">
      <div class="left-lahc">
        <input placeholder="name" v-model.trim="formItem.name" />
        <input placeholder="game" v-model.trim="formItem.game" />
        <input placeholder="type" v-model.trim="formItem.type" />
        <br />
        <button @click="formSubmit">submit</button>
      </div>
      <div class="right-lahc">
        <input placeholder="nameLikeOrId" v-model="nameLikeOrId" />
        <ul>
          <li v-for="it in filterList" @click="clickItem(it)">
            {{ it.name }}::{{ it.game }}
          </li>
        </ul>
      </div>
    </div>
    <div class="bottom-lach" v-if="item">
      <hr />
      <live-assistant-item-component
        :item="item"
      ></live-assistant-item-component>
    </div>
  </div>
</template>

<style scoped>
.ctn-lahc {
  .top-lahc {
    height: 120px;
    display: flex;

    .left-lahc {
      width: 250px;
      border-right: 1px solid black;
    }

    .right-lahc {
      flex: 1;
    }
  }
}
</style>
