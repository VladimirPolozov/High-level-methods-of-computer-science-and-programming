package app

import domain.enums.ExitCode
import kotlin.system.exitProcess


// Обрабатывает исключения и возвращает код завершение программы;
object ExitCodeProcessor {
    fun finish(code: ExitCode) : Nothing {
        exitProcess(code.code)
    }
}