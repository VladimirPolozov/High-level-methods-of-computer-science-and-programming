#!/bin/bash
set -e

mkdir -p out

./kotlinc/bin/kotlinc src \
  -cp "lib/kotlinx-cli-jvm-0.3.6.jar;lib/kotlin-reflect-1.9.0.jar;lib/kotlin-stdlib.jar" \
  -include-runtime \
  -d out/app.jar

jar cfe app.jar MainKt -C out .