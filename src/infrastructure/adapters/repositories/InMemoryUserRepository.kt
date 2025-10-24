// Implements IUserRepository: listOf<User> (из CreateUsers.kt, с хэшами)

package user

val users = listOf(
    User("alice", "alice123", listOf(
        Permissions("A", setOf("read", "write", "execute"))
    )),
    User("bob", "bob456", listOf(
        Permissions("A.B", setOf("read"))
    )),
    User("charlie", "charlie789", listOf(
        Permissions("A.B.C", setOf("write")),
        Permissions("A.B.G", setOf("execute"))
    )),
    User("diana", "diana000", listOf(
        Permissions("X", setOf("read")),
        Permissions("X.Y", setOf("write"))
    )),
    User("admin", "adminpass", listOf(
        Permissions("A", setOf("read", "write", "execute")),
        Permissions("X", setOf("read", "write", "execute"))
    ))
)