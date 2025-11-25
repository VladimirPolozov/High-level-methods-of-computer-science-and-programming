#!/bin/bash
set -e

echo "=== RUN ==="

bash build.sh

cd "$(dirname "$0")/.." || exit

java -Xdiag -cp "/out/app.jar;/lib/kotlinx-cli-jvm-0.3.6.jar;/lib/h2-2.4.240.jar" app.MainKt "$@"

echo "=== END RUN ==="