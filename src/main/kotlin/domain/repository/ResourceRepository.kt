package domain.repository

import domain.entities.Resource

interface ResourceRepository {
    fun findByPath(path: String): Resource?
}