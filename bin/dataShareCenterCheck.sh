#!/bin/bash
#
RUN_NAME="data-share-center"
#如果传入外部参数那么就采用第一个参数作为过滤的条件
if [ -n "$1" ]; then
  RUN_NAME=$1
fi
#echo "$RUN_NAME"
pid=`ps -ef|grep $RUN_NAME|grep java|grep -v grep|grep -v stop|awk '{print $2}'`
#echo $pid
if [ ! -n "$pid" ]; then
   echo "pid is NULL,Starting $RUN_NAME ...`date`"
   /opt/data-share-center-1.0/bin/startDataShareCenter.sh restart
else
   echo "$RUN_NAME is running...`date`"
fi