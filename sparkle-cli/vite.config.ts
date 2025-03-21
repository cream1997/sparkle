import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import electron from "vite-plugin-electron/simple";
// https://vite.dev/config/
export default defineConfig({
    plugins: [vue(),
        electron({
            main: {
                entry: 'electron/main.ts'
            },
            preload: {
                input: "electron/preload/preload.ts"
            }
        })],
})
