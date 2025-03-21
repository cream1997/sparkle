process.stdin.setEncoding('utf8');

/**
 * 这个脚本算是一个无奈之举；
 * 因为这两个警告(一个是打开devtool报的错；一个是input框输入内容时报的rgb错误，可能是focus的光圈背景的颜色不符合项目中的某个库的标准)
 * 想了很多办法都消除不掉，总之，这两个警告都不影响程序运行（大概率没事），所以就这样屏蔽了
 *
 * 使用这个脚本也有弊端，我目前发现的只有vite构建的原始输出发生了一些格式的变化，影响不大（比如重复日志后面的 数量标志没了(X2、X3..)）
 *
 * 在package.json中的dev2会调用这个脚本，dev就是原始，可以分别调用查看它们的细微差异
 * 将来项目如果日志出现了一些诡异问题，可以调用npm run dev(非dev2),来排除这个脚本的影响
 */
process.stdin.on('data', (chunk) => {
    // 要加这行，否则会多出一行空行
    chunk = chunk.slice(0, -1)
    if (!chunk.includes('Request Autofill') && !chunk.includes('libpng warning')) {
        console.log(chunk);
    }
});