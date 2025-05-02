#!/bin/bash

error_echo() {
    echo -e "\033[31m错误：$1\033[0m" >&2
}
# 检查root权限
if [ "$(id -u)" -ne 0 ]; then
    error_echo "必须使用root权限运行此脚本！"
    exit 1
fi


# 定义函数：配置全局彩色提示符（通过 /etc/profile.d/）
setup_global_prompt_color() {
    # 1. 创建独立配置文件 /etc/profile.d/custom_prompt.sh
    cat > /etc/profile.d/custom_prompt.sh << 'EOF'
#!/bin/bash

# 全局彩色提示符配置
if [ "$PS1" ]; then
    # 使用 tput 定义颜色（粉色加粗）
    PS1="$(tput bold)$(tput setaf 5)[\u@\h \W]\\$ $(tput sgr0)"
    export PS1
fi
EOF

    # 2. 设置权限（所有用户可读）
    chmod 644 /etc/profile.d/custom_prompt.sh

    # 3. 输出成功信息（带颜色）
    echo -e "\n\033[1;32m✔ 全局提示符颜色配置完成！\033[0m"
    echo -e "当前用户需重新登录生效，新用户将自动继承配置。"
}

# 调用彩色提示符配置
if ! setup_global_prompt_color; then
    error_echo "警告：提示符颜色配置失败，但脚本继续运行..."
fi