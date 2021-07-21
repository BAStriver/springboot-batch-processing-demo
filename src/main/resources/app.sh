#!/bin/sh
## java env
export JAVA_HOME=/usr/java/jdk1.8
export PATH=$JAVA_HOME/bin:$PATH
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar

API_NAME=/app_data/app/bas-0.0.1-SNAPSHOT
JAR_NAME=$API_NAME\.jar
#PID stand by pid file
PID=$API_NAME\.pid
#Get the second param
ENV=$2

#Use instructions to prompt for input parameters
usage() {
    echo "Usage: sh bas_parse_server.sh [start stg|start prod|start dr|stop|restart stg|restart prod|restart dr|status]"
    exit 1
}

#Check if the program is already running
is_exist(){
  pid=`ps -ef|grep $JAR_NAME|grep -v grep|awk '{print $2}' `
  #If there is no return 1, there is return 0
  if [ -z "${pid}" ]; then
   return 1
  else
    return 0
  fi
}

env=$2

#start method
start(){
  is_exist
  if [ $? -eq "0" ]; then
    echo ">>> ${JAR_NAME} is already running PID=${pid} <<<"
  else
    nohup java -Xms6144m -Xmx6144m -XX:ConcGCThreads=2 -XX:G1HeapRegionSize=32m -XX:+UseG1GC -Dspring.profiles.active=${ENV} -jar ${JAR_NAME} > /data/app_logs/bas_parse_server_console.log 2>&1 &
    echo $! > ${PID}
    echo ">>> start $JAR_NAME successed PID=$! <<<"
    sleep 20
    fi
  }

#stop method
stop(){
  #is_exist
  pidf=$(cat $PID)
  #echo "$pidf"
  echo ">>> api PID = $pidf begin kill $pidf <<<"
  kill $pidf
  rm -rf $PID
  sleep 2
  is_exist
  if [ $? -eq "0" ]; then
    echo ">>> api 2 PID = $pid begin kill -9 $pid  <<<"
    kill -9  $pid
    sleep 2
    echo ">>> $JAR_NAME process stopped <<<"
  else
    echo ">>> ${JAR_NAME} is not running <<<"
  fi
}

#Output running state
status(){
  is_exist
  if [ $? -eq "0" ]; then
    echo ">>> ${JAR_NAME} is running PID is ${pid} <<<"
  else
    echo ">>> ${JAR_NAME} is not running <<<"
  fi
}

#restart method
restart(){
  stop
  start
}

#According to the input parameters, select the corresponding method to execute, no input will execute the instructions
case "$1" in
  "start")
    case "$2" in
      "dev" | "stg" | "prod" | "dr")
        start
        ;;
      *)
        usage
        ;;
    esac
    ;;
  "stop")
    stop
    ;;
  "status")
    status
    ;;
  "restart")
    case "$2" in
      "dev" | "stg" | "prod" | "dr")
        restart
        ;;
      *)
        usage
        ;;
    esac
    ;;
  *)
    usage
    ;;
esac
exit 0