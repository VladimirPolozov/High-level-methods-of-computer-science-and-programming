package domain.entities


// Перечисление прав доступа: READ, WRITE, EXECUTE; содержит метод для безопасного парсинга из строки
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