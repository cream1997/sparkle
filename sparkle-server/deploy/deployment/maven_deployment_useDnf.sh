#!/bin/bash

# 1. 检查是否以root权限运行
if [ "$(id -u)" != "0" ]; then
    echo "❌ 错误：请使用 root 权限运行此脚本！"
    echo "👉 使用方式：sudo $0"
    exit 1
fi

# 2. 检查是否安装了 JDK 21
java_version=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d. -f1)
if [ "$java_version" != "21" ]; then
    echo "❌ 未检测到 JDK 21，请先安装 JDK 21 再运行此脚本。"
    echo "👉 执行命令：dnf install java-21-openjdk-devel"
    exit 1
fi

echo "✅ 检测到 JDK 21，开始安装 Maven（OpenJDK 21 绑定版）..."

# 3. 安装 Maven（绑定 JDK 21 的版本）
dnf install -y maven-openjdk21.noarch

if ! command -v mvn &> /dev/null; then
    echo "❌ Maven 安装失败，请检查网络或 DNF 仓库配置。"
    exit 1
fi

echo "🎉 Maven 安装成功！版本信息如下："
mvn -v

# 4. 配置 Maven
echo "⚙️ 配置 Maven..."

# 创建本地仓库目录
LOCAL_REPO="/data/MavenRepository"
mkdir -p "$LOCAL_REPO"
chown -R $(id -un):$(id -gn) "$LOCAL_REPO"

# 生成 settings.xml
MAVEN_SETTINGS="/etc/maven/settings.xml"  # 全局配置
mkdir -p /etc/maven

cat > "$MAVEN_SETTINGS" <<EOF
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">
    <!-- 本地仓库路径 -->
    <localRepository>$LOCAL_REPO</localRepository>

    <!-- 阿里云镜像 -->
    <mirrors>
        <mirror>
            <id>aliyun-maven</id>
            <mirrorOf>central</mirrorOf>
            <name>Aliyun Maven Mirror</name>
            <url>https://maven.aliyun.com/repository/public</url>
        </mirror>
    </mirrors>
</settings>
EOF

echo "✅ 配置完成！"
echo "   - 本地仓库: $LOCAL_REPO"
echo "   - 镜像源: 阿里云"