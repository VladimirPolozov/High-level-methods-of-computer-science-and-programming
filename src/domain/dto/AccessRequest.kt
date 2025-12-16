package domain.dto

import domain.enums.Action


// DTO для входного запроса: содержит логин, пароль, путь к ресурсу, действие и объём
data class AccessRequest (
    val login: String,
    val password: String,
    val path: String,
    val action: String,
    val volume: Int
)