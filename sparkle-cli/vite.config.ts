import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import electron from "vite-plugin-electron/simple";
import * as path from "node:path";
import vueDevTools from "vite-plugin-vue-devtools";
// https://vite.dev/config/
export default defineConfig(({ mode, command }) => {
  const isDev = mode === "development" && command === "serve";
  return {
    plugins: [
      vue(),
      vueDevTools(),
      electron({
        main: {
          entry: "electron/main.ts",
          vite: {
            build: {
              outDir: "build/main",
              sourcemap: isDev
            }
          },
          /**
           * 启动前做些事情...
           * 主要就是配置渲染进程调试端口
           */
          onstart: (args) => {
            // 启动electron 应用
            if (isDev) {
              args.startup([
                ".",
                "--no-sandbox",
                "--remote-debugging-port=9222"
              ]);
            } else {
              args.startup([".", "--no-sandbox"]);
            }
          }
        },
        preload: {
          input: "electron/preload/preload.ts",
          vite: {
            build: {
              outDir: "build/main",
              sourcemap: isDev
            }
          }
        }
      })
    ],
    resolve: {
      alias: {
        "@": path.resolve(__dirname, "src")
      }
    },
    build: {
      outDir: "build/renderer",
      sourcemap: isDev
    }
  };
});
