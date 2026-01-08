#!/bin/bash
set -e

cd "$(dirname "$0")/.."

mvn clean package -DskipTests

mkdir -p out

cp target/lab7-1.0-SNAPSHOT.jar out/app.jar

echo "=== BUILD SUCCESSFUL ==="
CP="lib/kotlinx-cli-jvm-0.3.6.jar:lib/kotlin-reflect-1.9.0.jar:lib/kotlin-stdlib.jar:lib/slf4j-api-2.0.9.jar:lib/slf4j-simple-2.0.17.jar:lib/h2-2.4.240.jar:lib/sqlite-jdbc-3.43.2.0.jar"

kotlinc src/ -cp "$CP" -include-runtime -d "out/app.jar"

echo "=== END BUILD ==="
