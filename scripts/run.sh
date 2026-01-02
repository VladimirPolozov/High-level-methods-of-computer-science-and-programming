#!/bin/bash
set -e

echo "=== RUN ==="

cd "$(dirname "$0")/.." || exit

java -Dfile.encoding=UTF-8 -jar out/app.jar "$@"

echo "=== END RUN ==="