#!/bin/bash

JAVA_HOME=/usr/lib/jvm/java-8-oracle
LOCAL_PATH=/home/ubuntu/octopus

jsvc_run()
{
    cd $1
    /usr/bin/jsvc -home $JAVA_HOME \
                  -cwd $1 \
                  -user ubuntu \
                  -debug \
                  -outfile std.out \
                  -errfile std.err \
                  -pidfile pid \
                  -cp $LOCAL_PATH/lib/commons-daemon-1.0.15.jar:$LOCAL_PATH/octopus-daemon.jar \
                  $2 \
                  com.chaos.octopus.Daemon
}

case $1 in
    start)
        echo "Starting Octopus Orchestrator"

        jsvc_run $2
    ;;
    stop)
        echo "Stopping Octopus Orchestrator"

        jsvc_run $2 "-stop"
    ;;
    *)
        echo "usage: (start|stop)"
esac
