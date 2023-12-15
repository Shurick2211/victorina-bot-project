package com.nimko.bot.repositories

import com.nimko.bot.models.ChannelEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface ChannelRepo:MongoRepository<ChannelEntity, String>  {
}