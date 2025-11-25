package application.services

import domain.entities.Resource
import domain.enums.ExitCode
import interfaces.VolumeValidator


// Реализует проверку объёма: разрешает, только если 0 ≤ volume ≤ maxVolume ресурса
class VolumeValidatorImpl : VolumeValidator {
    override fun validate(volume: Int, resource: Resource): ExitCode {
        if (volume < 0) return ExitCode.INVALID_FORMAT
        if (volume > resource.maxVolume) return ExitCode.EXCEED_MAX_VOLUME
        return ExitCode.SUCCESS
    }
}