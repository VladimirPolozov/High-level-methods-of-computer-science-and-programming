package domain.enums


// Коды завершения программы: SUCCESS(0) и ошибки (2–8), включая аутентификацию, доступ, формат и лимиты
enum class ExitCode(val code: Int) {
    SUCCESS(0),
    HELP(1),
    INVALID_PASSWORD(2),
    INVALID_LOGIN(3),
    UNKNOWN_ACTION(4),
    FORBIDDEN(5),
    NOT_FOUND(6),
    INVALID_FORMAT(7),
    EXCEED_MAX_VOLUME(8),
    DB_CONNECTION_ERROR(9),
    SQL_ERROR(10),
    OTHER_ERROR(12),
}
