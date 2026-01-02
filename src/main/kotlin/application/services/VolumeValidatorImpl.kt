package main.kotlin.application.services

import main.kotlin.domain.entities.Resource
import main.kotlin.domain.enums.ExitCode
import main.kotlin.domain.services.VolumeValidator
import org.springframework.stereotype.Service


// Реализует проверку объёма: разрешает, только если 0 ≤ volume ≤ maxVolume ресурса
@Service
class VolumeValidatorImpl : VolumeValidator {
    override fun validate(volume: Int, resource: Resource): ExitCode {
        if (volume < 0) return ExitCode.INVALID_FORMAT
        if (volume > resource.maxVolume) return ExitCode.EXCEED_MAX_VOLUME
        return ExitCode.SUCCESS
    }
}