import kotlin.system.exitProcess

// обрабатывает исключения и возвращает код завершение программы;

object ExitCodeProcessor {
    fun finish(code: ExitCode) {
        exitProcess(code.code)
    }
}