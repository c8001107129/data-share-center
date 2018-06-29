#!/bin/bash
#step1 部署项目
path=`pwd`
#要求执行脚本与程序代码压缩包必须同一个目录
#将程序代码移动到/opt 目录
mv ./data-share-center-1.0-full.zip /opt
cd /opt
#解压项目,-o参数如果存在就进行覆盖
unzip -o data-share-center-1.0-full.zip

#step2 增加程序系统开机启动
cmd1="/opt/data-share-center-1.0/bin/startDataShareCenter.sh restart"
rc_local=/etc/rc.d/rc.local
if [ `grep -c "$cmd1" ${rc_local}` -eq '0' ]; then
    echo "Not Found!,add $cmd1 to $rc_local"
    echo "$cmd1" >> ${rc_local}
    #增加可执行权限
    chmod +x ${rc_local}
else
    echo "rc.local Found!,No need to add"
fi

#step3 增加程序crontab定时监控 操作用户的执行计划：/var/spool/cron/root
cmd2="*/5 * * * * /opt/data-share-center-1.0/bin/dataShareCenterCheck.sh >> /opt/data-share-center-1.0/logs/check.log"
cron_root=/var/spool/cron/root
if [ `grep -c "/opt/data-share-center-1.0/bin/dataShareCenterCheck.sh" ${cron_root}` -eq '0' ]; then
    echo "Not Found!,add $cmd2 to $cron_root"
    echo "$cmd2" >> ${cron_root}
else
    echo "Crontab Found!,No need to add"
fi

#step4 开放端口
# （--permanent永久生效，没有此参数重启后失效）
firewall-cmd --zone=public --add-port=8088/tcp --permanent
#重新加载
firewall-cmd --reload


