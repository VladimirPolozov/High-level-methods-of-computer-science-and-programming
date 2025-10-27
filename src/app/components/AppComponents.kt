package app.components

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