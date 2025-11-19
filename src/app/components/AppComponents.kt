package app.components

import application.services.AccessControllerImpl
import application.services.AuthServiceImpl
import application.services.RequestProcessor
import application.services.VolumeValidatorImpl
import di.DatabaseManager
import infrastructure.adapters.repositories.InMemoryResourceRepositoryImpl
import infrastructure.adapters.repositories.InMemoryUserRepositoryImpl
import infrastructure.adapters.repositories.ResourceRepositoryImpl
import infrastructure.adapters.repositories.UserRepositoryImpl


// Фабрика компонентов: собирает и возвращает готовый RequestProcessor с in-memory репозиториями и сервисами
object AppComponents {
    private val connection = DatabaseManager.connect()

    fun createDefault(): RequestProcessor {
        val userRepo = UserRepositoryImpl(connection)
        val resourceRepo = ResourceRepositoryImpl(connection)

        val auth = AuthServiceImpl(userRepo)
        val access = AccessControllerImpl(resourceRepo)
        val volume = VolumeValidatorImpl()

        return RequestProcessor(auth, access, volume, resourceRepo)
    }
}