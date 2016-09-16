:: create shadowJar first
:: gradlew shadowJar
@echo off

SET /A COUNTER=1
SET DATABASE=database/database-longbatch

:while1

ECHO The counter is %COUNTER%

echo "[SCRIPT] Clear database %DATABASE%"
rmdir /s /q "%DATABASE%"

echo "[SCRIPT] Cleared"

echo "[SCRIPT] Spawning writter"
start %JAVA_HOME%\bin\java -Xmx4g -cp ethereumj-core/build/libs/ethereumj-core-1.4.0-SNAPSHOT-all.jar org.ethereum.db.TestLongBatchApplication

:: SET /a SECS=%RANDOM% * X / 32768 + Y
:: from Y to X
SET /a SECS=%RANDOM% * 20 / 32768 + 30
:: in the background, sleep for 10 secs then kill that process
echo "[SCRIPT] Sleep for %SECS% seconds and then kill process"
:: Timeout SECS
Timeout "%SECS%"
Taskkill /f /im java.exe
echo "[SCRIPT] Killed batch after %SECS% seconds"

echo "[SCRIPT] Sleep 10 secods to wait for resources to be available"
Timeout 10

%JAVA_HOME%\bin\java -cp ethereumj-core/build/libs/ethereumj-core-1.4.0-SNAPSHOT-all.jar org.ethereum.db.TestVerifyConsistencyApplication
SET EXIT_CODE=%errorlevel%

echo Exit code %EXIT_CODE%

if %EXIT_CODE%==0 (
	echo "[SCRIPT] Database is OK"
) else (
	SET NEW_DATABASE=%DATABASE%-corrupted-%COUNTER%
	echo "[SCRIPT] Database is corrupted backup it to %NEW_DATABASE%"
	rmdir /s /q "%NEW_DATABASE%"
	xcopy "%DATABASE%" "%NEW_DATABASE%" /s /h /e /k /f /c /i
)

echo "[SCRIPT] Sleep 5 seconds before new loop"
Timeout 5

SET /A COUNTER=COUNTER+1
GOTO :while1

