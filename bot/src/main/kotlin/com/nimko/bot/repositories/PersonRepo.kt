package com.nimko.bot.repositories

import com.nimko.bot.models.Person
import com.nimko.bot.utils.PersonRole
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface PersonRepo: MongoRepository<Person, String> {
    fun findAllByRole(role:PersonRole):List<Person>
}