#!/bin/bash

# 检查是否以root运行
if [ "$(id -u)" -ne 0 ]; then
    echo "请使用root用户或通过sudo运行此脚本！"
    exit 1
fi

# 安装必要软件包
echo "正在安装subversion和Apache..."
dnf install -y subversion httpd mod_dav_svn mod_ssl openssl

# 创建SVN仓库目录
SVN_DIR="/data/dev_repository"
REPO_NAME="dev_repo"
echo "正在创建SVN仓库目录: $SVN_DIR..."
mkdir -p "$SVN_DIR"
svnadmin create "$SVN_DIR/$REPO_NAME"
chown -R apache:apache "$SVN_DIR"

# 配置Apache
echo "正在配置Apache..."
cat > /etc/httpd/conf.d/subversion.conf <<EOF
LoadModule dav_svn_module     modules/mod_dav_svn.so
LoadModule authz_svn_module   modules/mod_authz_svn.so

<Location /svn>
    DAV svn
    SVNParentPath $SVN_DIR
    SVNListParentPath on

    # 启用基本认证
    AuthType Basic
    AuthName "SVN Repository"
    AuthUserFile /etc/svn-auth-users
    Require valid-user
</Location>
EOF

# 创建认证用户
AUTH_FILE="/etc/svn-auth-users"
echo "设置SVN认证用户（输入密码）..."
touch "$AUTH_FILE"
htpasswd -c -b "$AUTH_FILE" admin admin123  # 默认用户admin，密码admin123
chown apache:apache "$AUTH_FILE"

# 配置SELinux
echo "配置SELinux..."
setsebool -P httpd_unified 1
chcon -R -t httpd_sys_content_t "$SVN_DIR"

# 启动Apache
echo "启动Apache服务..."
systemctl enable --now httpd

# 防火墙放行HTTP/HTTPS
echo "配置防火墙..."
firewall-cmd --add-service={http,https} --permanent
firewall-cmd --reload

# 输出访问信息
SERVER_IP=$(hostname -I | awk '{print $1}')
echo "============================================"
echo "SVN服务已部署成功！"
echo "仓库地址: http://$SERVER_IP/svn/$REPO_NAME"
echo "默认用户: admin"
echo "默认密码: admin123"
echo "============================================"
