#!/bin/bash
set -e

bash "$(dirname "$0")/build.sh"

cd "$(dirname "$0")/.." || exit

DB_DIR="src/data"
mkdir -p "$DB_DIR"

CLASSPATH="out/app.jar"
CLASSPATH="${CLASSPATH};src/resources/"
CLASSPATH="${CLASSPATH};lib/kotlinx-cli-jvm-0.3.6.jar"
CLASSPATH="${CLASSPATH};lib/kotlin-reflect-1.9.0.jar"
CLASSPATH="${CLASSPATH};lib/kotlin-stdlib.jar"
CLASSPATH="${CLASSPATH};lib/slf4j-api-2.0.9.jar"
CLASSPATH="${CLASSPATH};lib/slf4j-simple-2.0.17.jar"
CLASSPATH="${CLASSPATH};lib/h2-2.4.240.jar"
CLASSPATH="${CLASSPATH};lib/sqlite-jdbc-3.43.2.0.jar"

java -cp "$CLASSPATH" MainKt "$@"
