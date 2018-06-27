#!/bin/bash

#Linux 网络流量监控脚本,时间间隔

if [ -n "$1" ]; then
	eth_name=$1
else
	eth_name="eth0"
fi
#path="`date +'%Y-%m-%d'`-net.txt"
#echo "Current Ip: "`/sbin/ifconfig $eth_name | grep inet`
#echo "Summry info: "`/sbin/ifconfig $eth_name | grep bytes`
#sleep 1 second ,monitor
#eth_name="ens160"
while true
do
	receive1=`cat /proc/net/dev|grep $eth_name | awk '{print $2}'`
	receive_pack1=`cat /proc/net/dev|grep $eth_name | awk '{print $3}'`
	send1=`cat /proc/net/dev|grep $eth_name | awk '{print $10}'`
	send_pack1=`cat /proc/net/dev|grep $eth_name | awk '{print $11}'`
	sleep 1
	receive2=`cat /proc/net/dev|grep $eth_name | awk '{print $2}'`
	receive_pack2=`cat /proc/net/dev|grep $eth_name | awk '{print $3}'`
	send2=`cat /proc/net/dev|grep $eth_name | awk '{print $10}'`
	send_pack2=`cat /proc/net/dev|grep $eth_name | awk '{print $11}'`

	receive_cnt=`expr $receive2 - $receive1`
	receive_pack_cnt=`expr $receive_pack2 - $receive_pack1`
	send_cnt=`expr $send2 - $send1`
	send_pack_cnt=`expr $send_pack2 - $send_pack1`
	#mem
	mem_used=`free -h | grep "Mem:" |awk '{print $3}'`
	mem_free=`free -h | grep "Mem:" |awk '{print $4}'`
	buff_cache=`free -h | grep "Mem:" |awk '{print $6}'`
	path="`date +'%Y-%m-%d'`-net.txt"
	echo "`date +'%Y-%m-%d %H:%M:%S'`-${eth_name} Receive Bytes:${receive_cnt},Receive Packets:${receive_pack_cnt},Send Bytes:${send_cnt},Send Packets:${send_pack_cnt},mem_used:${mem_used},mem_free:${mem_free},buff_cache:${buff_cache}" >> $path
	#echo "${eth_name} Receive Bytes:${receive_cnt} Receive Packets:${receive_pack_cnt}|Send Bytes:${send_cnt} Send Packets:${send_pack_cnt}"
done