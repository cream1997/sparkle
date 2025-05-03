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
    echo -e "${GREEN}[信息] 配置MySQL远程访问与密码策略...${NC}"
    CONFIG_FILES=("/etc/my.cnf" "/etc/mysql/my.cnf" "/etc/my.cnf.d/server.cnf")
    for file in "${CONFIG_FILES[@]}"; do
        if [ -f "$file" ]; then
            # 配置 bind-address
            if ! grep -q "^bind-address" "$file"; then
                echo "bind-address=0.0.0.0" >> "$file"
            else
                sed -i 's/^bind-address.*/bind-address=0.0.0.0/' "$file"
            fi

            # 添加或更新密码策略配置(否则重启密码策略就恢复成默认了)
            if ! grep -q "^validate_password.policy" "$file"; then
                echo "validate_password.policy=LOW" >> "$file"
            else
                sed -i 's/^validate_password.policy.*/validate_password.policy=LOW/' "$file"
            fi

            if ! grep -q "^validate_password.length" "$file"; then
                echo "validate_password.length=4" >> "$file"
            else
                sed -i 's/^validate_password.length.*/validate_password.length=4/' "$file"
            fi

            systemctl restart mysqld
            return 0
        fi
    done

    # 如果没有找到任何配置文件，则创建 /etc/my.cnf 并写入配置
    echo -e "${YELLOW}[警告] 创建新配置文件/etc/my.cnf${NC}"
    cat > /etc/my.cnf <<EOF
[mysqld]
bind-address=0.0.0.0
validate_password.policy=LOW
validate_password.length=4
EOF
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
    expect \"Change the password for root ? *\"
    send \"y\r\"
    expect \"New password:\"
    send \"123456\r\"
    expect \"Re-enter new password:\"
    send \"123456\r\"
    expect \"Do you wish to continue with the password provided?*\"
    send \"y\r\"
    expect \"Remove anonymous users? *\"
    send \"y\r\"
    expect \"Disallow root login remotely? *\"
    send \"n\r\"
    expect \"Remove test database and access to it? *\"
    send \"y\r\"
    expect \"Reload privilege tables now? *\"
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

    # 先用ALTER USER修改密码（必须第一步执行）,因为必须要先修改一次密码才能修改密码策略，这个临时密码要强健一点不然设置不成功
    mysql --connect-expired-password -u root -p"$TEMP_PASSWORD" -e \
    "ALTER USER 'root'@'localhost' IDENTIFIED BY 'TempPass@123';" 2>/dev/null
    TEMP_PASSWORD="TempPass@123"
    # 修改密码策略
    mysql --connect-expired-password -u root -p"$TEMP_PASSWORD" -e \
        "SET GLOBAL validate_password.policy=LOW; SET GLOBAL validate_password.length=4;" 2>/dev/null
    # 安全向导如果运行错误，就可以用这个临时密码登录
    echo -e "${RED} 临时root密码: $TEMP_PASSWORD${NC}"
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
