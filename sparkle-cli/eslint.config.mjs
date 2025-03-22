import {defineConfig} from "eslint/config";
import globals from "globals";
import js from "@eslint/js";
import tseslint from "typescript-eslint";
import pluginVue from "eslint-plugin-vue";


export default defineConfig([
    {files: ["**/*.{js,mjs,cjs,ts,vue}"]},
    {files: ["**/*.{js,mjs,cjs,ts,vue}"], languageOptions: {globals: {...globals.browser, ...globals.node}}},
    {files: ["**/*.{js,mjs,cjs,ts,vue}"], plugins: {js}, extends: ["js/recommended"]},
    tseslint.configs.recommended,
    pluginVue.configs["flat/essential"],
    {files: ["**/*.vue"], languageOptions: {parserOptions: {parser: tseslint.parser}}},
    {
        files: ["**/*.{js,mjs,cjs,ts,vue}"],
        // 禁用 @typescript-eslint/no-explicit-any 规则，允许使用 any 类型
        rules: {
            "@typescript-eslint/no-explicit-any": "off"
        }
    }
]);
