#### 代码在github上托管的同时托管到私有的gitea上

1. 查看当前远程仓库
   `git remote -v`
2. 添加远程仓库
   git remote add gitea http://本地IP:3000/用户名/项目名.git(如：git remote add
   gitea http://192.168.0.200:3000/cream/sparkle.git)
   再次运行 git remote -v，应该会看到两个远程
   origin  https://github.com/你的账号/项目名.git (fetch)
   origin  https://github.com/你的账号/项目名.git (push)
   gitea   http://本地IP:3000/用户名/项目名.git (fetch)
   gitea   http://本地IP:3000/用户名/项目名.git (push)
3. 首次推送需要指定分支（如 main 或 master）：
   `git push -u gitea main`
   后续推送可直接简写：`git push gitea`
4. 同时推送到两个仓库
   如果希望一条命令推送到两个远程，可以运行：
   `git push --all origin && git push --all gitea`
   或配置默认推送所有远程：
   git remote set-url --add --push origin http://本地IP:3000/用户名/项目名.git
   （但这种方式可能混淆来源，建议显式分开推送）
5. 从 Gitea 拉取代码
   如果需要从 Gitea 拉取更新：
   `git pull gitea main`

#### 注意事项

分支同步：确保两个远程仓库的分支结构一致，避免冲突。