#!/bin/sh
if [ -n "$JAVA_HOME" ];then
echo "JAVA_HOME已经存在"
echo "$JAVA_HOME"
else
echo "设置JAVA_HOME"
export JAVA_HOME="/opt/aspire/product/prm/jboss/jdk"
fi
