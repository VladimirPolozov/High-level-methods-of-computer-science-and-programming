#!/bin/bash

success=0
total=10

run_test() {
  description=$1
  expected=$2
  shift 2
  ./run.sh "$@"
  code=$?
  if [ $code -eq $expected ]; then
    echo "[$description] OK"
    success=$((success+1))
  else
    echo "[$description] FAIL (expected $expected, got $code)"
  fi
}

run_test "Успешное выполнение" 0 --login alice --password alice123 --action read --resource A.B --volume 10
run_test "Справка" 1 -h
run_test "Неверный пароль" 2 --login alice --password wrong --action read --resource A --volume 1
run_test "Неверный логин" 3 --login nobody --password xxx --action read --resource A --volume 1
run_test "Неизвестное действие" 4 --login alice --password alice123 --action hack --resource A --volume 1
run_test "Нет доступа" 5 --login bob --password bob456 --action write --resource A.B --volume 1
run_test "Несуществующий ресурс" 6 --login alice --password alice123 --action read --resource A.ZZ --volume 1
run_test "Некорректный формат" 7 --login alice --password alice123 --action read --resource "bad*name" --volume 1
run_test "Превышение объема" 8 --login alice --password alice123 --action read --resource A.B.C.f_d --volume 999
run_test "Успешное выполнение admin" 0 --login admin --password adminpass --action execute --resource X.Y --volume 5

echo "Результат: $success/$total"