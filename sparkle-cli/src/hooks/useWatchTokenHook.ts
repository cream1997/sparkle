import { onActivated, watchEffect, type WatchHandle } from "vue";
import Tip from "@/tools/Tip.ts";
import useAccountStore from "@/store/useAccountStore.ts";
import { onBeforeRouteLeave, useRoute, useRouter } from "vue-router";
import { HeroPagePath } from "@/router/RouterConst.ts";

const accountStore = useAccountStore();

export default function useWatchTokenHook() {
  const router = useRouter();
  const route = useRoute();

  let stopTokenWatch: WatchHandle;
  onActivated(() => {
    stopTokenWatch = watchEffect(() => {
      const token = accountStore.token;
      if (!token) {
        let tip = accountStore.tokenInvalidDesc;
        accountStore.tokenInvalidDesc = "";
        if (!tip) {
          tip = "token为空，请先登录";
        }
        Tip.err(tip, 800);
        const oldPath = route.path;
        setTimeout(() => {
          if (route.path === oldPath) {
            // 避免已经手动点击跳转到其他页面比如Music
            router.push(HeroPagePath.LoginGame);
          }
        }, 900);
      }
    });
  });

  onBeforeRouteLeave(() => {
    stopTokenWatch();
  });
}
