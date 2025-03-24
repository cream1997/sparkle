node版本v22.14.0, 其他相近的版本应该也没问题。

vite整合electron使用vite-plugin-electron(https://github.com/electron-vite/vite-plugin-electron)
可以参考(https://github.com/caoxiemeihao/electron-vite-samples/tree/main/quick-start)

vite-plugin-electron还有问题，就是dist-electron目录多出来的内容在
重新构建时不会删除，只是单纯的覆盖；也就是多出来的文件不会被删除，不过影响不大

### 打包

使用electron-builder打包

#### 注意1

windows打包需要管理员权限，不然一些链接可能无法创建(用管理员权限打开idea,idea的终端也就有管理员权限了)

#### 注意2

应用图标需要特殊处理,开发模式的图标和打包后它的处理模式不同；开发时，可以通过在创建窗口处指定图标，对图标格式等等要求都不高
但是打包后，可能是由于windows对exe程序的一些特定机制，需要将图标打包进exe,而且对图标的格式大小都有苛刻的要求,
这里用electron-icon-builder现将项目中的pong转为各个系统所需的格式，然后再在electron-builder的配置文件中指定图标

#### 主进程的调试（使用webstorm）

为了能在启动时调试主进程，需要在webstorm的【运行/调试】中添加node选项 --enable-source-maps ；
如果没有添加这个选项，无法在启动后动态的给ts源代码添加断点，只能在未启动时在ts代码中添加断点,启动后才生效，或者启动后要到构建的js代码中添加断点
