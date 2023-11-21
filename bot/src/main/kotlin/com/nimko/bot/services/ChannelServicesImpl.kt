package com.nimko.bot.services


import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Chat

@Service
class ChannelServicesImpl:ChannelServices {
    override fun saveChannel(channel: Chat) {
        println(channel)
    }
}