#!/bin/bash
_remote="85.214.86.158"
_user="root"

export JAVA_HOME=/c/Java/jdk-11/
export PATH=$PATH:/c/Java/jdk-11/bin

mvn clean compile assembly:single


echo
echo "*** Running commands on remote host named $_remote ***"
echo

echo "file copy"
scp target/GitHubSampler-1.0-jar-with-dependencies.jar root@85.214.86.158:/home/wwwuser/GitHubSampler-1.0.jar

