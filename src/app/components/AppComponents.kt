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
    private val connection = DatabaseManager.connect()
    private val logger = LoggerFactory.getLogger("DB_INIT")

    fun createDefault(): RequestProcessor {
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

fun initDatabase(connection: Connection) {
    val logger = LoggerFactory.getLogger("DB_INIT")
    logger.info("Начало создания структуры БД (встроенный SQL).")

    val schemaSql = """
        CREATE TABLE IF NOT EXISTS users (
            login VARCHAR(255) PRIMARY KEY,
            password_hash BLOB NOT NULL,
            salt BLOB NOT NULL
        );

        CREATE TABLE IF NOT EXISTS resources (
            path VARCHAR(255) PRIMARY KEY,
            name VARCHAR(255) NOT NULL,
            max_volume INT NOT NULL
        );

        CREATE TABLE IF NOT EXISTS permissions (
            user_login VARCHAR(255),
            resource_path VARCHAR(255),
            action VARCHAR(10) NOT NULL,
            PRIMARY KEY (user_login, resource_path, action),
            FOREIGN KEY (user_login) REFERENCES users(login),
            FOREIGN KEY (resource_path) REFERENCES resources(path)
        );
    """

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

    val dataSql = """
        -- ИСПРАВЛЕНО: Использование MERGE INTO для предотвращения ошибок нарушения ключа в H2
        MERGE INTO resources (path, name, max_volume) KEY(path) VALUES
        ('A', 'A', 100),
        ('A.B', 'B', 50),
        ('A.B.C', 'C', 20),
        ('A.B.C.f_d', 'f_d', 500),
        ('X.Y', 'Y', 1000);

        -- ИСПРАВЛЕНО: Использование MERGE INTO для предотвращения ошибок нарушения составного ключа в H2
        MERGE INTO permissions (user_login, resource_path, action) KEY(user_login, resource_path, action) VALUES
        ('alice', 'A.B', 'READ'),
        ('bob', 'A.B', 'READ'),
        ('alice', 'A.B.C.f_d', 'READ'),
        ('admin', 'X.Y', 'EXECUTE');
    """

    try {
        connection.createStatement().execute(dataSql)
        logger.info("Тестовые ресурсы и права доступа успешно добавлены.")
    } catch (e: SQLException) {
        logger.error("Ошибка при вставке тестовых данных: ${e.message}")
    }
}