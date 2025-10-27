package interfaces

import domain.entities.Resource
import domain.enums.ExitCode

// Интерфейс: validate(volume, resource): ExitCode (лимит объёма)

interface VolumeValidator {
    fun validate(volume: Int, resource: Resource): ExitCode
}