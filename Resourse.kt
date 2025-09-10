data class Resource(
    val name: String,
    val maxVolume: Int,
    val content: String? = null,
    val children: MutableList<Resource>
)

fun resource(name: String, max: Int, content: String? = null, init: MutableList<Resource>.() -> Unit = {}): Resource {
    val children = mutableListOf<Resource>()
    children.init()
    return Resource(name, max, content, children)
}

val root = resource("ROOT", Int.MAX_VALUE) {
    add(resource("A", 100) {
        add(resource("B", 80) {
            add(resource("C", 50) {
                add(resource("f_d", 30))
            })
            add(resource("G", 20))
        })
        add(resource("A8B", 40))
    })
}

