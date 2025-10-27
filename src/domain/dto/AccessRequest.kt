package domain.dto

import domain.entities.Action


// Data class: login, password, path, action: Action, volume: Int
data class AccessRequest (
    val login: String,
    val password: String,
    val path: String,
    val action: Action,
    val volume: Int
)