package resources

import domain.entities.Action
import domain.entities.Resource

object CreateResources {
    fun all(): Resource {
        val root = Resource("A", 100)
        val a8b = Resource("A8B", 200, parent = root)
        val c = Resource("C", 300, parent = a8b, permissions = mapOf("alice" to setOf(Action.READ, Action.WRITE)))
        val fd = Resource("f_d", 50, parent = c, permissions = mapOf("bob" to setOf(Action.EXECUTE)))
        return fd // leaf
    }
}