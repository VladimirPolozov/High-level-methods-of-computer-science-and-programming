package infrastructure.adapters.dto

/**
 * DTO для передачи данных о пользователе между БД и Маппером.
 * Соответствует структуре таблицы USERS.
 */
data class JdbcUserDto(
    val login: String,
    val passwordHash: ByteArray,
    val salt: ByteArray
    // Права доступа будут загружаться отдельно, т.к. хранятся в другой таблице.
)