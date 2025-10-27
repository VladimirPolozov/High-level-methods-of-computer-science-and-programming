package domain.enums


// Enum: SUCCESS(0), UNAUTHORIZED(2), FORBIDDEN(3), etc. (коды выхода)
enum class ExitCode(val code: Int) {
    SUCCESS(0),
    HELP(1),
    INVALID_PASSWORD(2),
    INVALID_LOGIN(3),
    UNKNOWN_ACTION(4),
    FORBIDDEN(5),
    NOT_FOUND(6),
    INVALID_FORMAT(7),
    EXCEED_MAX_VOLUME(8)
}
