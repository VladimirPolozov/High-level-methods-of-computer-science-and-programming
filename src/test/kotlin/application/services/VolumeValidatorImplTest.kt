package application.services

import domain.entities.Resource
import main.kotlin.domain.enums.ExitCode
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals


class VolumeValidatorImplTest {

    private val validator = VolumeValidatorImpl()
    private val maxVolume = 100

    private val testResource = Resource(path = "A.B.C", maxVolume = maxVolume)

    @Test
    fun `should return success when volume is within bounds`() {
        val volume = 50
        val result = validator.validate(volume, testResource)
        assertEquals(ExitCode.SUCCESS, result)
    }

    @Test
    fun `should return success when volume is zero`() {
        val result = validator.validate(0, testResource)
        assertEquals(ExitCode.SUCCESS, result)
    }

    @Test
    fun `should return success when volume equals max volume`() {
        val result = validator.validate(maxVolume, testResource)
        assertEquals(ExitCode.SUCCESS, result)
    }

    @Test
    fun `should return invalid format when volume is negative`() {
        val result = validator.validate(-1, testResource)
        assertEquals(ExitCode.INVALID_FORMAT, result)
    }

    @Test
    fun `should return exceed max volume when volume is too high`() {
        val result = validator.validate(maxVolume + 1, testResource)
        assertEquals(ExitCode.EXCEED_MAX_VOLUME, result)
    }
}