package app.components

import application.services.AccessControllerImpl
import application.services.AuthServiceImpl
import application.services.RequestProcessor
import application.services.VolumeValidatorImpl
import di.DatabaseManager
import domain.entities.Resource
import infrastructure.HashPassword
import infrastructure.adapters.repositories.InMemoryResourceRepositoryImpl
import infrastructure.adapters.repositories.InMemoryUserRepositoryImpl
import infrastructure.adapters.repositories.ResourceRepositoryImpl
import infrastructure.adapters.repositories.UserRepositoryImpl
import org.h2.tools.RunScript
import org.slf4j.LoggerFactory
import java.io.FileReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.sql.Connection
import java.sql.SQLException


// Фабрика компонентов: собирает и возвращает готовый RequestProcessor с in-memory репозиториями и сервисами

object AppComponents {
    private val connection = DatabaseManager.connect() // Предполагается, что DatabaseManager возвращает Connection
    private val logger = LoggerFactory.getLogger("DB_INIT")

    fun createDefault(): RequestProcessor {
        // Выполняем создание таблиц и вставку данных
        initDatabase(connection)
        initDefaultData(connection)

        val userRepo = UserRepositoryImpl(connection)
        val resourceRepo = ResourceRepositoryImpl(connection)

        val auth = AuthServiceImpl(userRepo)
        val access = AccessControllerImpl(resourceRepo)
        val volume = VolumeValidatorImpl()

        return RequestProcessor(auth, access, volume, resourceRepo)
    }

}

private fun readSqlFile(fileName: String): String {
    val filePath = "/db/$fileName"
    val inputStream = AppComponents::class.java.getResourceAsStream(filePath)
        ?: throw IllegalArgumentException("SQL файл не найден в Classpath: $filePath")

    return inputStream.bufferedReader().use { it.readText() }
}


fun initDatabase(connection: Connection) {
    val logger = LoggerFactory.getLogger("DB_INIT")
    logger.info("Начало создания структуры БД из файла init.sql")

    val schemaSql = readSqlFile("init.sql")

    try {
        connection.createStatement().execute(schemaSql)
        logger.info("Структура БД успешно создана.")
    } catch (e: SQLException) {
        logger.error("КРИТИЧЕСКАЯ ОШИБКА при создании таблиц: ${e.message}")
        throw e
    }
}

fun initDefaultData(connection: Connection) {
    val logger = LoggerFactory.getLogger("DB_INIT")
    logger.info("Начало инициализации тестовых пользователей и данных.")

    val users = listOf(
        "alice" to "alice123",
        "bob" to "bob456",
        "admin" to "adminpass"
    )

    try {
        // Логика вставки пользователей остается в Kotlin, так как здесь нужен цикл и хэширование.
        val checkStmt = connection.prepareStatement("SELECT COUNT(*) FROM users WHERE login = ?")
        val insertUserStmt = connection.prepareStatement(
            "INSERT INTO users (login, password_hash, salt) VALUES (?, ?, ?)"
        )
        for ((login, pass) in users) {
            checkStmt.setString(1, login)
            val rs = checkStmt.executeQuery()
            rs.next()
            if (rs.getInt(1) > 0) continue

            val salt = HashPassword.generateSalt()
            val hash = HashPassword.hash(pass, salt)

            insertUserStmt.setString(1, login)
            insertUserStmt.setBytes(2, hash)
            insertUserStmt.setBytes(3, salt)
            insertUserStmt.executeUpdate()
        }
        logger.info("Тестовые пользователи успешно добавлены.")
    } catch (e: SQLException) {
        logger.error("Ошибка при вставке пользователей: ${e.message}")
    }

    logger.info("Загрузка тестовых ресурсов и прав доступа из файла fill.sql.")

    val dataSql = readSqlFile("fill.sql")

    try {
        connection.createStatement().execute(dataSql)
        logger.info("Тестовые ресурсы и права доступа успешно добавлены.")
    } catch (e: SQLException) {
        logger.error("Ошибка при вставке тестовых данных: ${e.message}")
    }
}