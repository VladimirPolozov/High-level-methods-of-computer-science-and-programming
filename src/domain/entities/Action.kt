package domain.entities

// Enum: READ, WRITE, EXECUTE (права доступа)

enum class Action {
    READ, WRITE, EXECUTE;

    companion object {
        fun fromString(value: String): Action? = when (value.lowercase()) {
            "read" -> READ
            "write" -> WRITE
            "execute" -> EXECUTE
            else -> null
        }
    }
}