import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import electron from "vite-plugin-electron/simple";
import * as path from "node:path";
// https://vite.dev/config/
export default defineConfig(({ mode, command }) => {
  const sourcemap = mode === "development" && command === "serve";
  return {
    plugins: [
      vue(),
      electron({
        main: {
          entry: "electron/main.ts",
          vite: {
            build: {
              outDir: "build/main",
              sourcemap
            }
          }
        },
        preload: {
          input: "electron/preload/preload.ts",
          vite: {
            build: {
              outDir: "build/main",
              sourcemap
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
      sourcemap
    }
  };
});
