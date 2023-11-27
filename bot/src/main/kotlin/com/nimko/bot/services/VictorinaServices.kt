package com.nimko.bot.services

import com.nimko.bot.models.Victorina

interface VictorinaServices {

    fun getActiveVictorin():List<Victorina>

    fun saveRightAnsweredUserId(userId:String, victorinaId:String)

    fun saveWinner(userId:String, victorinaId:String)
}