let lastTimestamp = 0;
let counter = 0;

function geneId(): number {
  let timestamp = Date.now();

  if (timestamp === lastTimestamp) {
    counter = (counter + 1) & 0xfff; // 12位计数器
    if (counter === 0) {
      // 等待下一毫秒
      while (Date.now() <= timestamp) {
        /* 等待时间戳变化 */
      }
      timestamp = Date.now();
    }
  } else {
    counter = 0;
    lastTimestamp = timestamp;
  }

  return timestamp * 0x1000 + counter; // 左移12位
}

export default geneId;
