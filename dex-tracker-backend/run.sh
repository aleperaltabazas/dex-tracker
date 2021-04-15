#!/bin/bash

if [ $# -eq 0 ]; then
  echo "Usage: ./run <env>"
  exit -1
fi

set -e

ENVIRONMENT=$1

java -Dconfig.file="environments/$ENVIRONMENT/application.conf" -Dlogback.configurationFile=environments/"$ENVIRONMENT"/logback.xml -jar target/dex-jar-with-dependencies.jar
