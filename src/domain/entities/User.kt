package domain.entities


data class User(
    val login: String,
    val passwordHash: ByteArray,
    val salt: ByteArray,
    val permissions: Map<String, Set<Action>>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (login != other.login) return false
        if (!passwordHash.contentEquals(other.passwordHash)) return false
        if (!salt.contentEquals(other.salt)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = login.hashCode()
        result = 31 * result + passwordHash.contentHashCode()
        result = 31 * result + salt.contentHashCode()
        return result
    }
}