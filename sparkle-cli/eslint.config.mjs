import { defineConfig } from "eslint/config";
import globals from "globals";
import js from "@eslint/js";
import tseslint from "typescript-eslint";
import pluginVue from "eslint-plugin-vue";
import eslintConfigPrettier from "eslint-config-prettier/flat";

export default defineConfig([
  { ignores: ["dist*/*", "**/node_modules", "build/*", "release/*"] },
  { files: ["**/*.{js,mjs,cjs,ts,vue}"] },
  {
    files: ["**/*.{js,mjs,cjs,ts,vue}"],
    languageOptions: { globals: { ...globals.browser, ...globals.node } }
  },
  {
    files: ["**/*.{js,mjs,cjs,ts,vue}"],
    plugins: { js },
    extends: ["js/recommended"]
  },
  tseslint.configs.recommended,
  pluginVue.configs["flat/essential"],
  {
    files: ["**/*.vue"],
    languageOptions: { parserOptions: { parser: tseslint.parser } }
  },
  {
    files: ["**/*.{js,mjs,cjs,ts,vue}"],
    rules: {
      //----自定义规则----
      quotes: ["error", "double"], // 使用双引号
      semi: ["error", "always"], // 使用分号
      "no-unused-vars": "off",
      "@typescript-eslint/no-unused-vars": "off",
      // 禁用 @typescript-eslint/no-explicit-any 规则，允许使用 any 类型
      "@typescript-eslint/no-explicit-any": "off",
      "vue/no-unused-vars": "off",
      "linebreak-style": ["error", "unix"],
      // 禁用 vue/require-v-for-key 规则
      // todo 要确认v-for少了key具体有什么影响，这条配置后面可能需要去掉（视影响大小）
      "vue/require-v-for-key": "off"
    }
  },
  // 关闭所有不必要的或可能与Prettier冲突的规则(所以放在最后，总的来说就是让prettier来决定格式)
  eslintConfigPrettier
]);
