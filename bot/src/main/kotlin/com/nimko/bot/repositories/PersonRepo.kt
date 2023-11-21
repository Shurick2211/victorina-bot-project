package com.nimko.bot.repositories

import com.nimko.bot.models.Person
import org.springframework.data.mongodb.repository.MongoRepository

interface PersonRepo: MongoRepository<Person, String> {
}