#!/bin/bash
set -e

cd "$(dirname "$0")/.."

H2_JAR="lib/h2-2.4.240.jar" # Проверьте путь к вашему jar-файлу H2

mkdir -p "src/data"

DB_URL="jdbc:h2:./src/data/app-db"

echo "Инициализация схемы базы данных..."
java -cp "$H2_JAR" org.h2.tools.RunScript \
  -url "$DB_URL" \
  -script "scripts/init.sql"

echo "Заполнение тестовыми данными (fill.sql)..."
java -cp "$H2_JAR" org.h2.tools.RunScript \
  -url "$DB_URL" \
  -script "scripts/fill.sql"

echo "База данных успешно инициализирована: src/data/app-db.mv.db"
