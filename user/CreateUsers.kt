package user

val users = listOf(
    User("alice", "qwerty", listOf(
        Permission("A", setOf(Action.READ, Action.WRITE)),
        Permission("A.B.C", setOf(Action.EXECUTE))
    )),
    User("bob", "12345", listOf(
        Permission("X", setOf(Action.READ))
    ))
)
