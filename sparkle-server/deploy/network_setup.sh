#!/bin/bash

# 红色错误输出
error_echo() {
    echo -e "\033[31m错误：$1\033[0m" >&2
}

# 检查root权限
if [ "$(id -u)" -ne 0 ]; then
    error_echo "必须使用root权限运行此脚本！"
    exit 1
fi

# IP地址验证（四段式0-255）
validate_ip() {
    [[ $1 =~ ^[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}$ ]] || return 1
    IFS='.' read -r -a octets <<< "$1"
    for octet in "${octets[@]}"; do
        (( octet >= 0 && octet <= 255 )) || return 1
    done
    return 0
}

# 获取网络设备
get_network_device() {
    ip -o link show | awk -F': ' '$2 !~ "lo|vir" {print $2; exit}'
}

# 主程序
echo "===== 静态IP配置（子网掩码格式：255.255.255.0） ====="

# 获取网卡
device=$(get_network_device)
[ -z "$device" ] && { error_echo "未找到可用网卡"; exit 1; }
echo "检测到网卡: $device"

# 输入IP
while true; do
    read -p "请输入IP地址（如 192.168.1.100）: " ip
    validate_ip "$ip" && break || error_echo "IP格式错误！"
done

# 固定子网掩码为255.255.255.0
netmask="255.255.255.0"
echo "子网掩码已设置为: $netmask"

# 输入网关
while true; do
    read -p "请输入网关（如 192.168.1.1）: " gateway
    validate_ip "$gateway" && break || error_echo "网关格式错误！"
done

# 输入DNS
while true; do
    read -p "请输入DNS（如 223.5.5.5）: " dns
    validate_ip "$dns" && break || error_echo "DNS格式错误！"
done

# 生成配置文件
config_file="/etc/sysconfig/network-scripts/ifcfg-$device"
cat > "$config_file" <<EOF
TYPE=Ethernet
PROXY_METHOD=none
BROWSER_ONLY=no
BOOTPROTO=static
DEFROUTE=yes
IPV4_FAILURE_FATAL=no
NAME=$device
DEVICE=$device
ONBOOT=yes

IPADDR=$ip
NETMASK=$netmask
GATEWAY=$gateway
DNS1=$dns
EOF

# 应用配置
if nmcli connection reload && nmcli connection up "$device"; then
    echo -e "\n\033[32m配置成功！\033[0m"
    echo "IP地址: $(ip -o -4 addr show $device | awk '{print $4}')"
    echo "网关: $(ip route show | grep default | awk '{print $3}')"
else
    error_echo "配置应用失败！请检查："
    echo "1. 网线是否连接"
    echo "2. 网关是否可达"
    exit 1
fi