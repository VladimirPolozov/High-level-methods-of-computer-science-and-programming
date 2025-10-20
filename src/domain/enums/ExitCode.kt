// Enum: SUCCESS(0), UNAUTHORIZED(2), FORBIDDEN(3), etc. (коды выхода)

enum class ExitCode(val code: Int) {
    SUCCESS(0),
    INVALID_FORMAT(7),
    UNAUTHORIZED(2),
    USER_NOT_FOUND(3),
    INVALID_ACTION(4),
    PERMISSION_DENIED(5),
    RESOURCE_NOT_FOUND(6),
    INVALID_VOLUME(7),
    VOLUME_EXCEEDED(8);

    companion object {
        fun exit(code: ExitCode): Nothing {
            kotlin.system.exitProcess(code.code)
        }
    }
}