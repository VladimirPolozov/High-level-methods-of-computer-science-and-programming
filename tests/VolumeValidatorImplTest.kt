package tests

import application.services.VolumeValidatorImpl
import domain.entities.Resource
import domain.enums.ExitCode
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals


class VolumeValidatorImplTest {

    private val validator = VolumeValidatorImpl()
    private val maxVolume = 100
    private val testResource = Resource(name = "Test", maxVolume = maxVolume)

    @Test
    fun shouldReturnSuccessWhenVolumeIsWithinBounds() {
        val volume = 50
        val result = validator.validate(volume, testResource)
        assertEquals(ExitCode.SUCCESS, result, "Объем 50 должен быть разрешен")
    }

    @Test
    fun shouldReturnSuccessWhenVolumeIsZero() {
        val volume = 0
        val result = validator.validate(volume, testResource)
        assertEquals(ExitCode.SUCCESS, result, "Объем 0 должен быть разрешен")
    }

    @Test
    fun shouldReturnSuccessWhenVolumeEqualsMaxVolume() {
        val volume = maxVolume
        val result = validator.validate(volume, testResource)
        assertEquals(ExitCode.SUCCESS, result, "Объем, равный максимальному, должен быть разрешен")
    }

    @Test
    fun shouldReturnInvalidFormatWhenVolumeIsNegative() {
        val volume = -1
        val result = validator.validate(volume, testResource)
        assertEquals(ExitCode.INVALID_FORMAT, result, "Отрицательный объем должен возвращать INVALID_FORMAT")
    }

    @Test
    fun shouldReturnExceedMaxVolumeWhenVolumeIsTooHigh() {
        val volume = maxVolume + 1
        val result = validator.validate(volume, testResource)
        assertEquals(ExitCode.EXCEED_MAX_VOLUME, result, "Объем, превышающий максимальный, должен возвращать EXCEED_MAX_VOLUME")
    }
}