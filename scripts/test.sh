#!/bin/bash

echo "=== STARTING FUNCTIONAL TESTS ==="

cd "$(dirname "$0")"

echo "--- Очистка тестовой БД ---"

DB_DIR="../src/main/kotlin/data"
rm -rf "$DB_DIR"/*
mkdir -p "$DB_DIR"
echo "Директория БД $DB_DIR очищена."

echo "--- Сборка проекта ---"
bash "./init-db.sh"
bash "./build.sh"

success=0
total=10

run_test() {
  description=$1
  expected=$2
  shift 2

  echo "--- TEST: $description ---"

  bash "./run.sh" "$@"
  code=$?

  if [ $code -eq "$expected" ]; then
    echo "[PASS] Ожидалось $expected, получено $code."
    success=$((success+1))
  else
    echo "[FAIL] $description - ОШИБКА (Ожидалось $expected, получено $code)"
  fi
  echo "------------------------------"
}

echo "Running tests..."

run_test "Успешное выполнение (bob READ A.B)" 0 \
    --login bob \
    --password bob456 \
    --action read \
    --resource A.B \
    --volume 1 \

run_test "Запрос справки (-h)" 1 -h

run_test "Ошибка аутентификации (неверный пароль)" 2 \
    --login alice --password wrong --action read --resource A.B --volume 1

run_test "Ошибка аутентификации (неизвестный пользователь)" 3 \
    --login nobody --password xxx --action read --resource A.B --volume 1

run_test "Неизвестное действие (hack)" 4 \
    --login alice --password alice123 --action hack --resource A.B --volume 1

run_test "Нет доступа (alice WRITE A.B)" 5 \
    --login alice --password alice123 --action write --resource A.B --volume 1

run_test "Несуществующий ресурс (A.ZZ)" 6 \
    --login alice --password alice123 --action read --resource A.ZZ --volume 1

run_test "Некорректный формат ресурса" 7 \
    --login alice --password alice123 --action read --resource A-B --volume 1

run_test "Превышение объема" 8 \
    --login alice --password alice123 --action read --resource A.B --volume 9999

run_test "Успешное выполнение (alice READ A.B)" 0 \
    --login alice --password alice123 --action read --resource A.B --volume 5

echo "--------------------------------------------------------"
echo "Итоговый результат: $success/$total тестов пройдено."

if [ "$success" -eq "$total" ]; then
    echo "=== ALL TESTS PASSED ==="
    exit 0
else
    echo "=== SOME TESTS FAILED ==="
    exit 1
fi