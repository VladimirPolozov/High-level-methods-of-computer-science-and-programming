package domain.services

import domain.entities.Resource
import main.kotlin.domain.enums.ExitCode

interface VolumeValidator {
    fun validate(volume: Int, resource: Resource): ExitCode
}