#!/bin/bash
set -e

rm -rf out
mkdir -p out

CP=(
  "lib/kotlinx-cli-jvm-0.3.6.jar"
  "lib/kotlin-reflect-1.9.0.jar"
  "lib/kotlin-stdlib.jar"
  "lib/junit-jupiter-api-6.0.1.jar"
  "lib/junit-jupiter-engine-6.0.1.jar"
  "lib/junit-platform-console-standalone-6.0.1.jar"
)

kotlinc src \
  -cp "$(IFS=';'; echo "${CP[*]}")" \
  -include-runtime \
  -d out/app.jar