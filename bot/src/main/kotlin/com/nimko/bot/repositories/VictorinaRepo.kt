package com.nimko.bot.repositories


import com.nimko.bot.models.Victorina
import org.springframework.data.mongodb.repository.MongoRepository

interface VictorinaRepo: MongoRepository<Victorina,String>{

}