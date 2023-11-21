package com.nimko.bot.services

import org.telegram.telegrambots.meta.api.objects.Chat

interface ChannelServices {
    fun saveChannel(channel:Chat)
}