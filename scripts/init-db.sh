#!/bin/bash
set -e

cd "$(dirname "$0")/.."

mkdir -p "src/main/kotlin/data"

DB_URL="jdbc:h2:file:./src/main/kotlin/data/app-db"

echo "=== DATABASE INITIALIZATION ==="

mvn exec:java -Dexec.mainClass="org.h2.tools.RunScript" \
  "-Dexec.args=-url $DB_URL -user sa -script scripts/init.sql"

echo "Заполнение тестовыми данными..."
mvn exec:java -Dexec.mainClass="org.h2.tools.RunScript" \
  "-Dexec.args=-url $DB_URL -user sa -script scripts/fill.sql"

echo "=== DATABASE READY ==="
echo "Файлы базы данных созданы в: src/main/kotlin/data/"
