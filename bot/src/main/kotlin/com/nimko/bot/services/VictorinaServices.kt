package com.nimko.bot.services

import com.nimko.bot.models.VictorinaDto

interface VictorinaServices {

    fun getActiveVictorin():List<VictorinaDto>

    fun saveRightAnsweredUserId(userId:String, victorinaId:String)

    fun saveWinner(userId:String, victorinaId:String)

    fun getVictorinaById(id:String):VictorinaDto

    fun getEndedVictorinsMarcAsActive():List<VictorinaDto>

    fun getVictorinaByWinnerId(winnerId:String):VictorinaDto

    fun addParticipants(personId: String, victorinaId: String)

    fun startVictorinsAndIsActiveTrueAllVictorinas():List<VictorinaDto>
}