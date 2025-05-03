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

# 配置阿里云镜像源（适用于 AlmaLinux）
setup_yum_mirror() {
    echo "正在备份并替换 AlmaLinux 镜像源为阿里云..."

    # 替换镜像源地址
    if sed -e 's|^mirrorlist=|#mirrorlist=|g' \
           -e 's|^# baseurl=https://repo.almalinux.org|baseurl=https://mirrors.aliyun.com|g' \
           -i.bak \
           /etc/yum.repos.d/almalinux*.repo; then
        echo -e "\033[1;32m✔ 镜像源替换成功\033[0m"
    else
        error_echo "错误：镜像源替换失败"
        return 1
    fi

    # 更新 dnf 缓存
    echo "正在更新 dnf 缓存..."

    if dnf makecache; then
        echo -e "\033[1;32m✔ dnf 缓存更新完成\033[0m"
    else
        error_echo "错误：dnf 缓存更新失败"
        return 1
    fi
}

# 检查并安装 vim
install_vim() {
    echo "正在检查是否已安装 vim..."

    # 检查是否已安装
    if command -v vim &>/dev/null; then
        echo -e "\033[1;32m✔ vim 已安装\033[0m"
        return 0
    else
        echo -e "\033[33m⚠ 未检测到 vim，正在尝试安装...\033[0m"

        # 使用 dnf 安装
        if dnf install -y vim; then
            echo -e "\033[1;32m✔ vim 安装成功\033[0m"
        else
            error_echo "错误：vim 安装失败"
            return 1
        fi
    fi
}

# 检查并安装 JDK 21
check_install_jdk21() {
    echo "正在检查是否已安装 JDK 21..."

    # 检查是否已安装
    java_version=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d. -f1)
    if [ "$java_version" = "21" ]; then
        echo -e "\033[1;32m✔ JDK 21 已安装\033[0m"
        return 0
    else
        echo -e "\033[33m⚠ 未检测到 JDK 21，正在尝试安装...\033[0m"

        # 使用 dnf 安装 OpenJDK 21
        if dnf install -y java-21-openjdk-devel; then
            echo -e "\033[1;32m✔ JDK 21 安装成功\033[0m"
        else
            error_echo "错误：JDK 21 安装失败"
            return 1
        fi
    fi
}

# 配置 JDK 环境变量
setup_java_env() {
    echo "正在配置 JDK 环境变量..."

    cat > /etc/profile.d/custom_java.sh << 'EOF'
export JAVA_HOME=$(readlink -f /usr/bin/java | sed "s:/bin/java::")
export PATH=$JAVA_HOME/bin:$PATH
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
EOF

    # 设置权限
    chmod 644 /etc/profile.d/custom_java.sh

    # 立即加载环境变量
    source /etc/profile.d/custom_java.sh

    echo -e "\033[1;32m✔ JDK 环境变量配置完成！\033[0m"
}

# 全局彩色提示符配置
if ! setup_global_prompt_color; then
    error_echo "警告：提示符颜色配置失败，但脚本继续运行..."
fi

# yum镜像源配置
if ! setup_yum_mirror; then
    error_echo "警告：YUM 镜像源配置失败，但脚本继续运行..."
fi

# vim安装
if ! install_vim; then
    error_echo "警告：vim 安装失败，但脚本继续运行..."
fi

# 检查并安装 JDK 21
if ! check_install_jdk21; then
    error_echo "警告：JDK 21 安装失败，但脚本继续运行..."
fi

# 配置 JDK 环境变量
if ! setup_java_env; then
    error_echo "警告：JDK 环境变量配置失败，但脚本继续运行..."
fi