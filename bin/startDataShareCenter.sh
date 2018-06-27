#!/bin/bash
cd `dirname $0`
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`
#切换目录
cd $BIN_DIR
#执行名称
RUN_NAME="data-share-center"

start() {
 echo "$RUN_NAME trying to start ......`date`"
 #开启器jmx远程监控-Dcom.sun.management.jmxremote.port=1099 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Djava.rmi.server.hostname=192.168.35.44
 JAVA_OPTS=" -Djava.net.preferIPv4Stack=true "
 JAVA_MEM_OPTS=""
 BITS=`java -version 2>&1 | grep -i 64-bit`
 if [ -n "$BITS" ]; then
    JAVA_MEM_OPTS=" -server -XX:+TieredCompilation -Xms2048m -Xmx8192m -XX:+AggressiveOpts -XX:+UseG1GC -XX:+UseStringDeduplication "
 else
    JAVA_MEM_OPTS=" -server -Xms2048m -Xmx4096m "
 fi
 nohup java -D$RUN_NAME $JAVA_OPTS $JAVA_MEM_OPTS -jar ../lib/data-share-center-1.0.jar >/dev/null 2>&1 &
 echo "$RUN_NAME started success.`date`"
}

stop() {
  echo "Stopping $RUN_NAME ...`date`"
  kill -9 `ps -ef|grep $RUN_NAME|grep java|grep -v grep|grep -v stop|awk '{print $2}'`
}

case "$1" in
        start)
          start
          ;;
        stop)
          stop
          ;;
        restart)
          stop
          start
          ;;
        *)
        echo $"Usage: $0 {start|stop|restart}"
        exit 1
esac
