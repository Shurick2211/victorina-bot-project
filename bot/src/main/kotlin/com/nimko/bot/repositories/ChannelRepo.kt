package com.nimko.bot.repositories

import com.nimko.bot.models.Channel
import org.springframework.data.mongodb.repository.MongoRepository

interface ChannelRepo:MongoRepository<Channel, String>  {
}