#!/bin/bash
#

logfile=/opt/softwareag/IntegrationServer/instances/default/logs/server.log
echo "Waiting for the Integration Server log file"
while [ ! -f $logfile ]
do
	echo -n "."
	sleep 5
done
echo; echo "Waiting for Integration Server to become ONLINE"
while [ -z "`grep 5555 $logfile`" ]
do
	echo -n "."
	sleep 5
done
echo; echo "Integration Server is ONLINE"
