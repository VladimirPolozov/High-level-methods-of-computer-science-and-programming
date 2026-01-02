#!/bin/bash
set -e

cd "$(dirname "$0")/.."

mvn clean package -DskipTests

mkdir -p out

cp target/lab7-1.0-SNAPSHOT.jar out/app.jar

echo "=== BUILD SUCCESSFUL ==="