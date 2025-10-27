// Интерфейс: validate(volume, resource): ExitCode (лимит объёма)

interface VolumeValidator {
    fun validate(volume: Int, resource: String): ExitCode
}