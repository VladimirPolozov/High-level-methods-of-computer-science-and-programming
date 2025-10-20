// Data class: login, password, path, action: Action, volume: Int

import

data class AccessRequest (
    val login: String,
    val password: String,
    val path: String,
    val action: Action,
    val volume: Int,
)