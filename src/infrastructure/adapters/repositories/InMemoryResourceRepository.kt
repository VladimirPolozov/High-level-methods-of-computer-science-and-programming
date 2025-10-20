// Implements IResourceRepository: root Resource, buildTree (parent-ссылки, из CreateResources.kt)

package resources

val oot = Resource("ROOT", Int.MAX_VALUE, children = mutableListOf(
    Resource("A", 100, children = mutableListOf(
        Resource("B", 80, children = mutableListOf(
            Resource("C", 50, children = mutableListOf(
                Resource("f_d", 30)
            )),
            Resource("G", 20)
        )),
        Resource("A8B", 40)
    )),
    Resource("X", 60, children = mutableListOf(
        Resource("Y", 10)
    ))
))