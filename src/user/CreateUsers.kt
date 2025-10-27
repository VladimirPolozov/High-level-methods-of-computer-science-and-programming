package user

object CreateUsers {
    fun all(): List<User> {
        val salt1 = HashPassword.generateSalt()
        val salt2 = HashPassword.generateSalt()
        return listOf(
            User("alice", HashPassword.hash("qwerty", salt1), salt1),
            User("bob", HashPassword.hash("12345", salt2), salt2)
        )
    }
}