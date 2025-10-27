package resources

//data class Resource(
//    val name: String,
//    val maxVolume: Int,
//    val content: String? = null,
//    val children: MutableList<Resource> = mutableListOf()
//) {
//    fun findResource(path: String): Resource? {
//        if (path.isEmpty()) return this
//
//        val parts = path.split('.')
//        var current: Resource = this
//
//        for (part in parts) {
//            val child = current.children.find { it.name == part }
//                ?: return null // путь не существует
//            current = child
//        }
//        return current
//    }
//}