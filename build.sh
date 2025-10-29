#!/bin/bash
set -e

# Чистим старый билд
rm -rf out
mkdir -p out

# Компиляция проекта в JAR
kotlinc src \
  -cp "lib/kotlinx-cli-jvm-0.3.6.jar;lib/kotlin-reflect-1.9.0.jar;lib/kotlin-stdlib.jar" \
  -include-runtime \
  -d out/app.jar