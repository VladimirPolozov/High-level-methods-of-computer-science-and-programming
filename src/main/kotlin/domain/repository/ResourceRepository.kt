package main.kotlin.domain.repository

import main.kotlin.domain.entities.Resource

interface ResourceRepository {
    fun findByPath(path: String): Resource?
}