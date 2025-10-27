import resources.CreateResources
import resources.Resource

// Implements IResourceRepository: root Resource, buildTree (parent-ссылки, из CreateResources.kt)

class InMemoryResourceRepository : ResourceRepository {
    private val root = CreateResources.all()

    override fun findByPath(path: String): Resource? {
        val parts = path.split(".")
        return findRecursive(root, parts)
    }

    private fun findRecursive(current: Resource?, parts: List<String>): Resource? {
        if (current == null) return null
        if (current.name == parts.last()) return current
        return findRecursive(current.parent, parts)
    }
}