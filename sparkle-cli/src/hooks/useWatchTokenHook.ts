import { onActivated, onDeactivated, watchEffect, type WatchHandle } from "vue";
import Tip from "@/tools/Tip.ts";
import { HeroPagePath } from "@/router/router.ts";
import useAccountStore from "@/store/useAccountStore.ts";
import { useRoute, useRouter } from "vue-router";

const accountStore = useAccountStore();

export default function useWatchTokenHook() {
  const router = useRouter();
  const route = useRoute();

  let stopTokenWatch: WatchHandle;
  onActivated(() => {
    stopTokenWatch = watchEffect(() => {
      const token = accountStore.token;
      if (!token) {
        Tip.err("token为空，请先登录", 800);
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

  onDeactivated(() => {
    stopTokenWatch();
  });
}
