#!/bin/bash

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
NC='\033[0m' # 恢复默认颜色

# 检查是否以 root 运行
if [ "$(id -u)" -ne 0 ]; then
    echo -e "${RED}错误：此脚本必须以 root 用户运行！${NC}"
    echo -e "请使用 ${YELLOW}sudo ./install_git.sh${NC} 或切换到 root 用户后执行。"
    exit 1
fi

# 检查 Git 是否已安装
if command -v git &> /dev/null; then
    echo -e "${RED}错误：Git 已经安装！${NC}"
    echo -e "当前版本：$(git --version)"
    echo -e "如需重新配置，请直接运行 ${GREEN}git config${NC} 命令。"
    exit 1
fi

# 安装 Git
echo -e "\n${GREEN}[1/3] 正在安装 Git...${NC}"
dnf update -y && dnf install git -y

# 验证安装
if ! command -v git &> /dev/null; then
    echo -e "${RED}Git 安装失败，请检查网络或日志！${NC}"
    exit 1
else
    echo -e "✅ ${GREEN}Git 安装成功！版本：$(git --version)${NC}"
fi

# 配置 Git
echo -e "\n${GREEN}[2/3] 配置 Git 用户信息${NC}"
#read -p "请输入 Git 用户名（如 Your Name）: " git_username
#read -p "请输入 Git 邮箱（如 your.email@example.com）: " git_email
git_username="zjj"
git_email="xxx@qq.com"

git config --global user.name "$git_username"
git config --global user.email "$git_email"
git config --global init.defaultBranch main  # 设置默认分支

# 显示配置
echo -e "\n${GREEN}[3/3] Git 全局配置已设置：${NC}"
git config --global --list

# 完成提示
echo -e "\n🎉 ${GREEN}Git 安装与配置完成！${NC}"
echo -e "常用命令示例："
echo -e "  克隆仓库: ${YELLOW}git clone https://github.com/用户名/仓库.git${NC}"
echo -e "  提交代码: ${YELLOW}git add . && git commit -m '描述' && git push${NC}"