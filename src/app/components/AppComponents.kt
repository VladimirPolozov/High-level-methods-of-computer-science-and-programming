package app.components

import application.services.AccessControllerImpl
import application.services.AuthServiceImpl
import application.services.RequestProcessor
import application.services.VolumeValidatorImpl
import infrastructure.adapters.repositories.InMemoryResourceRepository
import infrastructure.adapters.repositories.InMemoryUserRepository

object AppComponents {
    fun createDefault(): RequestProcessor {
        val userRepo = InMemoryUserRepository()
        val resourceRepo = InMemoryResourceRepository()

        val auth = AuthServiceImpl(userRepo)
        val access = AccessControllerImpl(resourceRepo)
        val volume = VolumeValidatorImpl()

        return RequestProcessor(auth, access, volume, resourceRepo)
    }
}