#!/usr/bin/env bash
# create shalowJar first
# ./gradlew shadowJar

# Expecting 1 argument - path to database

DATABASE=$1

echo "Verify path database/$DATABASE"

java -cp ethereumj-core/build/libs/ethereumj-core-1.4.0-SNAPSHOT-all.jar org.ethereum.db.TestVerifyConsistencyApplication $DATABASE
EXIT_CODE=$?

echo $EXIT_CODE

if [ $EXIT_CODE = 0 ]; then
    echo "[SCRIPT] Database is OK"
else
    echo "[SCRIPT] Database is corrupted"
fi


