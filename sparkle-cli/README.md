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

### 主进程的调试（使用webstorm）

为了能在启动时调试主进程，需要在webstorm的【运行/调试】中添加node选项 --enable-source-maps ；
如果没有添加这个选项，无法在启动后动态的给ts源代码添加断点，只能在未启动时在ts代码中添加断点,启动后才生效，或者启动后要到构建的js代码中添加断点

### 渲染进程的调试

最简单的是直接使用Devtools，也就是F12打开后，找到源代码，然后直接加断点(vite要配置sourcemap)
但这不太方便(对我来说)，所以要配置在ide中如何调试，以下针对webstorm

1. 第一步是要配置electron的启动参数--remote-debugging-port=9222， 也就是启动时执行类型这样的命令electron .
   --remote-debugging-port=9222
2. 第二部是在webstorm中启动一个附加到Node.js/Chrome的调试器，端口指定为以上配置的端口
3. 以debug方式分别启动项目，和附加的调试器
   针对第一步的设置调试端口，对于本项目来说，可以在vite.config.js中配置(查看对应文件的electron插件配置)

#### ※※※ 关于WebStorm中调试渲染进程的单文件组件(xxx.vue) ※※※

> 如果你习惯使用chrome的devtool, 就不用管下面这个问题，因为在chrome的开发者工具中一切都是正常的

我在使用webstorm调试渲染进程的过程中发现了一个问题, 当我调试ts代码时没有任何问题，无论是热部署还是断点生效；
但是调试xxx.vue的script中的代码时发现,断点时而生效，时而无效(在webstorm中加断点不起作用);而且在不同的xxx.vue文件中表现还不相同，
有的完全正常，有的断点加不上；排查了很久，发现在chrome开发者工具中的vue源文件与本地不完全相同，
有些代码被转换成类似data-v-inspector="src/hero/pages/LoginPage.vue:43:5"这样的形式；
但是ai说这不影响调试，起初我也这么认为；因为有些完全正常的xxx.vue中也有很多地方被转换这种形式，也就是说尽管代码不完全对应，很多地方也是正常的
但是折腾了很久解决不了，我不想跳到chrome的devtool去调试(在我眼里那太麻烦了，也许是强迫症！)；尝试性的猜测是不是代码不完全对应导致的webstorm或者
debug工具无法处理映射，验证的方法就是我标签中的代码不要转换成data-v-inspector这种形式；查阅资料发现这个貌似是实现vite的devtools的Toggle
Component Inspector功能所需要的转换，关于这个组件审查功能貌似就是选中元素然后打开本地源文件，感觉这功能本身就很鸡肋(
你可以在vue的devtools中试试)

在引入插件的地方做如下配置，就可以关闭这个功能
vueDevTools({
// 配置为false
componentInspector: false
})
这样代码就不会做额外的转换了；到目前为止webstorm中单文件组件的调试一切正常