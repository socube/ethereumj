#!/usr/bin/env bash

# create shadowJar first
# ./gradlew shadowJar

COUNTER=1
DATABASE=database/database-longbatch


while [  $COUNTER -gt 0 ]; do
	echo The counter is $COUNTER

	# Clear database
	echo "[SCRIPT] Clear database"
	rm -Rf $DATABASE

	echo "[SCRIPT] Cleared"

	echo "[SCRIPT] Spawning writter"
	java -Xmx4g -cp ethereumj-core/build/libs/ethereumj-core-1.4.0-SNAPSHOT-all.jar org.ethereum.db.TestLongBatchApplication & pid=$!

	SECS=$(( ( RANDOM % 10 )  + 30 ))
	# in the background, sleep for 10 secs then kill that process
	echo "[SCRIPT] Sleep for $SECS seconds and then kill process"
	(sleep $SECS && echo "[SCRIPT] Killed batch after $SECS seconds" && kill -9 $pid)

	echo "[SCRIPT] Sleep 10 secods to wait for resources to be available"
	sleep 10

	# EXIT_CODE=$(java -cp ethereumj-core/build/libs/ethereumj-core-1.4.0-SNAPSHOT-all.jar org.ethereum.db.TestVerifyConsistencyApplication)
	java -cp ethereumj-core/build/libs/ethereumj-core-1.4.0-SNAPSHOT-all.jar org.ethereum.db.TestVerifyConsistencyApplication
	EXIT_CODE=$?

	echo $EXIT_CODE

	if [ $EXIT_CODE = 0 ]; then
		echo "[SCRIPT] Database is OK"
	else
		NEW_DATABASE="$DATABASE-corrupted-$COUNTER"
		echo "[SCRIPT] Database is corrupted backup it to $NEW_DATABASE"
		cp -R $DATABASE $NEW_DATABASE
	fi

	echo "[SCRIPT] Sleep 5 seconds before new loop"
	sleep 5

	let COUNTER=COUNTER+1 
done

