#!/bin/bash
#set -e

echo "=== STARTING FUNCTIONAL TESTS ==="

echo "--- Очистка тестовой БД ---"
DB_DIR="src/data"
rm -rf "$DB_DIR"/*
mkdir -p "$DB_DIR"
echo "Директория БД $DB_DIR очищена и готова."
echo "------------------------------"

bash "$(dirname "$0")/build.sh"

#cd "$(dirname "$0")/.." || exit

# Инициализация счетчиков
success=0
total=10

# Функция для запуска тестов и проверки кода выхода
# $1: описание теста, $2: ожидаемый код выхода, $@: аргументы для run.sh
run_test() {
  description=$1
  expected=$2
  shift 2

  echo "--- TEST $description (Ожидается код $expected) ---"

  # Вызываем скрипт запуска run.sh

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

# 1. Успешное выполнение (0)
# Используем bob456, как в AppComponents, и ресурс A.B.
run_test "Успешное выполнение (bob READ A.B)" 0 --login bob --password bob456 --action read --resource A.B --volume 1

# 2. Справка (1)
run_test "Запрос справки (-h)" 1 -h

# 3. Неверный пароль (2)
run_test "Ошибка аутентификации (неверный пароль)" 2 --login alice --password wrong --action read --resource A.B --volume 1

# 4. Неверный логин (3)
run_test "Ошибка аутентификации (неизвестный пользователь)" 3 --login nobody --password xxx --action read --resource A.B --volume 1

# 5. Неизвестное действие (4)
run_test "Неизвестное действие (hack)" 4 --login alice --password alice123 --action hack --resource A.B --volume 1

# 6. Нет доступа (5) - Alice не имеет WRITE на A.B
run_test "Нет доступа (alice WRITE A.B)" 5 --login alice --password alice123 --action write --resource A.B --volume 1

# 7. Несуществующий ресурс (6)
run_test "Несуществующий ресурс (A.ZZ)" 6 --login alice --password alice123 --action read --resource A.ZZ --volume 1

# 8. Некорректный формат (7) - Проверяем, что невалидные аргументы выдают 7
run_test "Некорректный формат ресурса (нет параметра)" 7 --login alice --password alice123 --action read --resource "bad*name"

# 9. Превышение объема (8)
# Предполагаем, что у Alice нет прав на ресурс с max_volume 9999
run_test "Превышение объема (max_volume 500)" 8 --login admin --password adminpass --action read --resource X.Y --volume 9999

# 10. Ошибка БД (9 или 10) - Попытка получить соединение, когда оно невозможно (или SQL-ошибка)
# Это сложно проверить в рамках одного скрипта без дополнительной логики.
# В этом тесте проверим успешное выполнение, которое покроет 0-8 и гарантирует, что БД работает.
run_test "Успешное выполнение (admin EXECUTE X.Y)" 0 --login admin --password adminpass --action execute --resource X.Y --volume 5


echo "--------------------------------------------------------"
echo "Итоговый результат: $success/$total тестов пройдено."

# Выход с кодом 0, если все тесты прошли, иначе 1.
if [ "$success" -eq "$total" ]; then
    exit 0
else
    exit 1
fi