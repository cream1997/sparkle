// 将 Map 转换为数组
export function mapToArr(map: Map<any, any>) {
  return Array.from(map.entries());
}

// 将数组转换为 Map
export function arrToMap(arr: []) {
  return new Map(arr);
}

/**
 * 字符串加密
 * fixme 这里可能会有一个字符信息丢失的问题，需要仿照后端的函数实现修改
 */
export function jiaMi(plainText: string, key: string): string {
  let encrypted = "";
  for (let i = 0; i < plainText.length; i++) {
    const charCode = plainText.charCodeAt(i) ^ key.charCodeAt(i % key.length);
    encrypted += String.fromCharCode(charCode);
  }
  return encrypted;
}

/**
 * 字符串解密
 * fixme 这里可能会有一个字符信息丢失的问题，需要仿照后端的函数实现修改
 */
export function jieMi(encryptedText: string, key: string): string {
  // 异或加密和解密使用相同的操作
  return jiaMi(encryptedText, key);
}
