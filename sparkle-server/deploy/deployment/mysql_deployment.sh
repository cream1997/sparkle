#!/bin/bash

# 检查是否为 root 用户
if [ "$(id -u)" -ne 0 ]; then
    echo "请使用 root 用户或通过 sudo 运行此脚本！"
    exit 1
fi

# 定义颜色变量
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

# 创建mysql组,安装依赖组件
install_prepare() {
    echo -e "${GREEN}[信息] 安装依赖工具...${NC}"
    # 安装expect
    if ! command -v expect &> /dev/null; then
        dnf install -y expect || apt-get install -y expect
    fi
    # 安装tcl
    if ! command -v tclsh &> /dev/null; then
        dnf install -y tcl || apt-get install -y tcl
    fi
}

# 修改MySQL配置
configure_mysql() {
    echo -e "${GREEN}[信息] 配置MySQL远程访问...${NC}"
    # 兼容不同配置文件路径
    CONFIG_FILES=("/etc/my.cnf" "/etc/mysql/my.cnf" "/etc/my.cnf.d/server.cnf")
    for file in "${CONFIG_FILES[@]}"; do
        if [ -f "$file" ]; then
            sed -i 's/^bind-address.*/bind-address = 0.0.0.0/' "$file"
            systemctl restart mysqld
            return 0
        fi
    done
    # 未找到配置文件时新建
    echo -e "${YELLOW}[警告] 创建新配置文件/etc/my.cnf${NC}"
    echo -e "[mysqld]\nbind-address = 0.0.0.0" > /etc/my.cnf
    systemctl restart mysqld
}

# 安全配置向导
secure_installation() {
    echo -e "${GREEN}[信息] 自动化安全配置...${NC}"
    # 使用expect处理交互
    expect -c "
    set timeout 30
    spawn mysql_secure_installation
    expect \"Enter password for user root:\"
    send \"$TEMP_PASSWORD\r\"
    expect \"New password:\"
    send \"123456\r\"
    expect \"Re-enter new password:\"
    send \"123456\r\"
    expect \"Change the password for root ? *\"
    send \"y\r\"
    expect \"Remove anonymous users? *\"
    send \"y\r\"
    expect \"Disallow root login remotely? *\"
    send \"n\r\"
    expect \"Remove test database? *\"
    send \"y\r\"
    expect \"Reload privilege tables? *\"
    send \"y\r\"
    expect eof
    "
}

# 主安装流程
install_mysql() {
    install_prepare

    echo -e "${GREEN}[信息] 开始安装MySQL 8...${NC}"
    rpm -Uvh https://dev.mysql.com/get/mysql80-community-release-el9-1.noarch.rpm
    dnf module disable -y mysql
    rpm --import https://repo.mysql.com/RPM-GPG-KEY-mysql-2023
    dnf install -y mysql-community-server

    systemctl enable --now mysqld
    TEMP_PASSWORD=$(grep 'temporary password' /var/log/mysqld.log | awk '{print $NF}')
    echo -e "${RED}[重要] 临时root密码: $TEMP_PASSWORD${NC}"

    # 修改密码策略
    mysql --connect-expired-password -u root -p"$TEMP_PASSWORD" -e \
        "SET GLOBAL validate_password.policy=LOW; SET GLOBAL validate_password.length=4;" 2>/dev/null

    secure_installation

    configure_mysql

    # 配置远程root访问
    mysql -u root -p123456 -e "
        CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED BY '123456';
        GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;
        FLUSH PRIVILEGES;
    " 2>/dev/null

    # 防火墙配置
    if systemctl is-active --quiet firewalld; then
        firewall-cmd --permanent --add-port=3306/tcp
        firewall-cmd --reload
    else
        echo -e "${YELLOW}[警告] firewalld未运行，请手动开放3306端口${NC}"
    fi

    echo -e "${GREEN}[成功] MySQL 8安装完成！${NC}"
    echo -e "${RED}[安全警告] 生产环境请立即修改默认密码！${NC}"
}

# 执行安装
install_mysql