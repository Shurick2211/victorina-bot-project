package com.nimko.bot.services

import com.nimko.bot.models.VictorinaDto
import com.nimko.bot.repositories.VictorinaRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Service
class VictorinaServicesImpl @Autowired constructor(
    private val victorinaRepo: VictorinaRepo
):VictorinaServices {

    var list:List<VictorinaDto> = emptyList()
    override fun getActiveVictorin(): List<VictorinaDto> {
        val today = LocalDateTime.now()
        list = victorinaRepo.findAllByEndDateAfter(today).filter { it.startDate.isBefore(today) }
        return list
    }

    override fun saveRightAnsweredUserId(userId: String, victorinaId:String) {
        val victorina = victorinaRepo.findById(victorinaId).get()
        if(victorina.rightsAnsweredUserId == null)
            victorina.rightsAnsweredUserId = ArrayList()
        victorina.rightsAnsweredUserId!!.add(userId)
        runBackgroundSaveState { victorinaRepo.save(victorina) }
    }

    override fun saveWinner(userId: String, victorinaId: String) {
        val victorina = victorinaRepo.findById(victorinaId).get()
        victorina.winnerId = userId
        runBackgroundSaveState { victorinaRepo.save(victorina) }
    }

    override fun getVictorinaById(id: String): VictorinaDto {
        return list.firstOrNull { it.id == id } ?: getActiveVictorin().first{it.id == id}
    }

    override fun getEndedVictorinsMarcAsActive(): List<VictorinaDto> = victorinaRepo.findAllByIsActiveIsTrueAndHasPrizeIsTrueAndEndDateBefore(
        LocalDateTime.now()).filter { it.winnerId == null }

    override fun getVictorinaByWinnerId(winnerId: String): VictorinaDto {
        val victorina = victorinaRepo.findByIsActiveIsTrueAndWinnerId(winnerId)
        victorina.isActive = false
        runBackgroundSaveState { victorinaRepo.save(victorina) }
        return victorina
    }

    override fun addParticipants(personId: String, victorinaId: String) {
        runBackgroundSaveState {
            val victorina = victorinaRepo.findById(victorinaId).get()
            if(victorina.participantsId == null) victorina.participantsId = ArrayList()
            victorina.participantsId!!.add(personId)
            victorinaRepo.save(victorina)
        }
    }

    override fun startChanellAndIsActiveTrueAllVictorinas(): List<VictorinaDto> {
        val today = LocalDateTime.now()
        val listStart = victorinaRepo.findAllByIsActiveIsFalseAndEndDateAfter(today)
            .filter { it.startDate.isBefore(today) }
        runBackgroundSaveState {
            listStart.forEach {
                if(!it.isActive) {
                    val victorina = victorinaRepo.findById(it.id!!).get()
                    victorina.isActive = true
                    victorinaRepo.save(victorina)
                }
            }
        }
        return listStart.filter { it.hasPrize && it.channel != null }
    }


    private fun runBackgroundSaveState(func:() -> Unit) = runBlocking {
        launch(Dispatchers.IO) {
            func.invoke()
        }
    }


}