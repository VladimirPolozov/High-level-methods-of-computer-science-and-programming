package app.components

import application.services.AccessControllerImpl
import application.services.AuthServiceImpl
import application.services.RequestProcessor
import application.services.VolumeValidatorImpl
import infrastructure.adapters.repositories.InMemoryResourceRepositoryImpl
import infrastructure.adapters.repositories.InMemoryUserRepositoryImpl


// Фабрика компонентов: собирает и возвращает готовый RequestProcessor с in-memory репозиториями и сервисами
object AppComponents {
    fun createDefault(): RequestProcessor {
        val userRepo = InMemoryUserRepositoryImpl
        val resourceRepo = InMemoryResourceRepositoryImpl

        val auth = AuthServiceImpl(userRepo)
        val access = AccessControllerImpl(resourceRepo)
        val volume = VolumeValidatorImpl()

        return RequestProcessor(auth, access, volume, resourceRepo)
    }
}