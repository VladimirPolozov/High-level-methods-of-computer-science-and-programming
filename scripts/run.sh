#!/bin/bash
set -e

echo "=== RUN ==="

#bash "$(dirname "$0")/build.sh"

cd "$(dirname "$0")/.." || exit

CLASSPATH="out/app.jar"
CLASSPATH="${CLASSPATH};src/resources/"
CLASSPATH="${CLASSPATH};lib/kotlinx-cli-jvm-0.3.6.jar"
CLASSPATH="${CLASSPATH};lib/kotlin-reflect-1.9.0.jar"
CLASSPATH="${CLASSPATH};lib/kotlin-stdlib.jar"
CLASSPATH="${CLASSPATH};lib/slf4j-api-2.0.9.jar"
CLASSPATH="${CLASSPATH};lib/slf4j-simple-2.0.17.jar"
CLASSPATH="${CLASSPATH};lib/h2-2.4.240.jar"
CLASSPATH="${CLASSPATH};lib/sqlite-jdbc-3.43.2.0.jar"

java -Dfile.encoding=UTF-8 -cp "$CLASSPATH" MainKt "$@"

echo "=== END RUN ==="