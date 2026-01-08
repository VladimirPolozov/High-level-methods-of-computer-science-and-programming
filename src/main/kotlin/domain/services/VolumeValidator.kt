package main.kotlin.domain.services

import main.kotlin.domain.entities.Resource
import main.kotlin.domain.enums.ExitCode

interface VolumeValidator {
    fun validate(volume: Int, resource: Resource): ExitCode
}