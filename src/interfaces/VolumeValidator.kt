package interfaces

import domain.entities.Resource
import domain.enums.ExitCode


// Интерфейс валидатора объёма: проверяет, укладывается ли запрашиваемый объём в лимит ресурса, возвращает код результата
interface VolumeValidator {
    fun validate(volume: Int, resource: Resource): ExitCode
}