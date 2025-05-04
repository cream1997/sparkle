#!/bin/bash
# 下载maven可能会很慢，建议本地下载好上传到服务器然后注释下载的部分

# 检查root权限
if [ "$(id -u)" != "0" ]; then
    echo "❌ 请使用 root 权限运行！"
    echo "👉 执行方式: sudo $0"
    exit 1
fi

# 检查JDK 21是否安装
java_version=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d. -f1)
if [ "$java_version" != "21" ]; then
    echo "❌ 未检测到 JDK 21，请先安装 JDK 21。"
    exit 1
fi

# 下载 Maven 3.9.9
MAVEN_URL="https://dlcdn.apache.org/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.tar.gz"
echo "⬇️ 下载 Maven 3.9.9..."
wget -q "$MAVEN_URL" -O /tmp/maven.tar.gz || {
    echo "❌ 下载失败！请检查网络或手动下载：$MAVEN_URL"; exit 1
}

# 安装到 /opt/maven
tar -xzf /tmp/maven.tar.gz -C /opt
mv /opt/apache-maven-3.9.9 /opt/maven
rm -f /tmp/maven.tar.gz

# 全局环境变量
cat > /etc/profile.d/maven.sh <<EOF
export MAVEN_HOME="/opt/maven"
export PATH="\$PATH:\$MAVEN_HOME/bin"
EOF
source /etc/profile.d/maven.sh

# 配置 Maven 的 settings.xml
MAVEN_DEFAULT_CONFIG="/opt/maven/conf/settings.xml"
cp "$MAVEN_DEFAULT_CONFIG" "${MAVEN_DEFAULT_CONFIG}.bak"  # 备份原始文件

# 步骤 1: 在注释块结束后插入 <localRepository>
sed -i '/<!-- localRepository/,/-->/ {/-->/a\
  <localRepository>/data/MavenRepository</localRepository>
}' "$MAVEN_DEFAULT_CONFIG"

# 步骤 2: 完全替换原有 <mirrors> 块为阿里云镜像
sed -i '/[[:space:]]*<mirrors>/,/[[:space:]]*<\/mirrors>/c\
  <mirrors>\
    <mirror>\
      <id>aliyun-maven</id>\
      <mirrorOf>central</mirrorOf>\
      <name>Aliyun Maven Mirror</name>\
      <url>https://maven.aliyun.com/repository/public</url>\
    </mirror>\
  </mirrors>' "$MAVEN_DEFAULT_CONFIG"

# 创建仓库目录
mkdir -p /data/MavenRepository
# todo 权限后续修改
chmod 777 /data/MavenRepository

echo "✅ 安装完成！"
mvn -v
echo -e "\n🔧 配置详情："
echo "   - Maven 路径: /opt/maven"
echo "   - 全局配置: $MAVEN_GLOBAL_CONFIG"
echo "   - 本地仓库: /data/MavenRepository"