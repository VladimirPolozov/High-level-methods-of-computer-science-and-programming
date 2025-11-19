#!/bin/bash
set -e

JAVA_EXE="/c/Program Files/JetBrains/IntelliJ IDEA 2025.1/jbr/bin/java"
"$JAVA_EXE" -cp "../out/app.jar:../lib/kotlinx-cli-jvm-0.3.6.jar:../lib/kotlin-reflect-1.9.0.jar:../lib/kotlin-stdlib.jar" MainKt "$@"