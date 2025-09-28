#!/bin/bash
set -e



java -cp "out/app.jar;lib/kotlin-reflect-1.9.0.jar;lib/kotlinx-cli-jvm-0.3.6.jar;lib/kotlin-stdlib.jar" MainKt "$@"

#java -jar out/app.jar MainKt "$@"