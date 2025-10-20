package user

data class User(
    val login: String,
    val passwordHash: ByteArray
    val salt: ByteArray,
)