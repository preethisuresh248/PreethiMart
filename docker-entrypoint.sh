#!/bin/sh
set -e

PORT_TO_USE="${PORT:-8080}"
echo "Configuring PreethiMart Tomcat to listen on port ${PORT_TO_USE}..."

# Update HTTP connector port in Tomcat server.xml
sed -i "s/port=\"8080\"/port=\"${PORT_TO_USE}\"/g" /usr/local/tomcat/conf/server.xml

echo "Starting Apache Tomcat 9 for PreethiMart..."
exec catalina.sh run
