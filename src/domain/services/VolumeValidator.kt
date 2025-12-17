package domain.services

import domain.entities.Resource
import domain.enums.ExitCode

interface VolumeValidator {
    fun validate(volume: Int, resource: Resource): ExitCode
}