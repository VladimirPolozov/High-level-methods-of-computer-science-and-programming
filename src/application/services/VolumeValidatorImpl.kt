import resources.Resource

// Implements VolumeValidator: volume <= maxVolume && >=0

class VolumeValidatorImpl : VolumeValidator {
    override fun validate(volume: Int, resource: Resource): ExitCode {
        if (volume < 0) return ExitCode.INVALID_FORMAT
        if (volume > resource.maxVolume) return ExitCode.EXCEED_MAX_VOLUME
        return ExitCode.SUCCESS
    }
}