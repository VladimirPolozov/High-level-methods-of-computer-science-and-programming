#!/bin/bash
#set -e

echo "=== STARTING FUNCTIONAL TESTS ==="

echo "--- Очистка тестовой БД ---"

DB_DIR="../src/data"
rm -rf "$DB_DIR"/*
mkdir -p "$DB_DIR"
echo "Директория БД $DB_DIR очищена и готова."
echo "------------------------------"

bash "$(dirname "$0")/init-db.sh"

bash "$(dirname "$0")/build.sh"

success=0
total=10

run_test() {
  description=$1
  expected=$2
  shift 2

  echo "--- TEST $description (Ожидается код $expected) ---"

  bash "$(dirname "$0")/run.sh" "$@"
  code=$?

  if [ $code -eq "$expected" ]; then
    echo "[PASS] [$description] - OK. Получен код $code."
    success=$((success+1))
  else
    echo "[FAIL] [$description] - ОШИБКА (Ожидалось $expected, Получено $code)"
  fi
}

echo "Running 10 standard tests..."
echo "--------------------------------------------------------"

run_test "Успешное выполнение (bob READ A.B)" 0 \
    --login bob \
    --password bob456 \
    --action read \
    --resource A.B \
    --volume 1

run_test "Запрос справки (-h)" 1 \
    -h

run_test "Ошибка аутентификации (неверный пароль)" 2 \
    --login alice \
    --password wrong \
    --action read \
    --resource A.B \
    --volume 1

run_test "Ошибка аутентификации (неизвестный пользователь)" 3 \
    --login nobody \
    --password xxx \
    --action read \
    --resource A.B \
    --volume 1 \

run_test "Неизвестное действие (hack)" 4 \
    --login alice \
    --password alice123 \
    --action hack \
    --resource A.B \
    --volume 1 \

run_test "Нет доступа (alice WRITE A.B)" 5 \
    --login alice \
    --password alice123 \
    --action write \
    --resource A.B \
    --volume 1

run_test "Несуществующий ресурс (A.ZZ)" 6 \
    --login alice \
    --password alice123 \
    --action read \
    --resource A.ZZ \
    --volume 1

run_test "Некорректный формат ресурса " 7 \
    --login alice \
    --password alice123 \
    --action read \
    --resource A-B\
    --volume 1 \

run_test "Превышение объема (max_volume 500)" 8 \
    --login alice \
    --password alice123 \
    --action read \
    --resource A.B \
    --volume 9999 \

run_test "Успешное выполнение (admin EXECUTE X.Y)" 0 \
    --login alice \
    --password alice123 \
    --action read \
    --resource A.B \
    --volume 5 \


echo "--------------------------------------------------------"
echo "Итоговый результат: $success/$total тестов пройдено."

if [ "$success" -eq "$total" ]; then
    exit 0
else
    exit 1
fi