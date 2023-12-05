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
    val victorinaRepo: VictorinaRepo
):VictorinaServices {

    lateinit var list:List<VictorinaDto>
    override fun getActiveVictorin(): List<VictorinaDto> {
        val today = LocalDateTime.now()
        list = victorinaRepo.findAllByEndDateAfter(today).filter { it.startDate.isBefore(today) }
        runBackgroundSaveState {
            list.forEach {
                if(!it.isActive) {
                    val victorina = victorinaRepo.findById(it.id!!).get()
                    victorina.isActive = true
                    victorinaRepo.save(victorina)
                }
            }
        }
        return list
    }

    override fun saveRightAnsweredUserId(userId: String, victorinaId:String) {
        val victorina = victorinaRepo.findById(victorinaId).get()
        if(victorina.rightsAnsweredUserId == null)
            victorina.rightsAnsweredUserId = ArrayList()
        victorina.rightsAnsweredUserId!!.add(userId)
        victorinaRepo.save(victorina)
    }

    override fun saveWinner(userId: String, victorinaId: String) {
        val victorina = victorinaRepo.findById(victorinaId).get()
        victorina.winnerId = userId
        victorinaRepo.save(victorina)
    }



    override fun getVictorinaById(id: String): VictorinaDto {
        return list.first{ it.id == id }
    }

    override fun getEndedVictorinsMarcAsActive(): List<VictorinaDto> = victorinaRepo.findAllByIsActiveIsTrueAndHasPrizeIsTrueAndEndDateBefore(
        LocalDateTime.now())

    override fun getOwnerVictorinaIdByWinnerId(winnerId: String): VictorinaDto {
        val victorina = victorinaRepo.findByIsActiveIsTrueAndWinnerId(winnerId)
        victorina.isActive = false
        victorinaRepo.save(victorina)
        return victorina
    }

    override fun addParticipants(victorinaId: String) {
        runBackgroundSaveState {
            val victorina = victorinaRepo.findById(victorinaId).get()
            victorina.numberParticipants += 1
            victorinaRepo.save(victorina)
        }
    }


    fun runBackgroundSaveState(func:() -> Unit) = runBlocking {
        launch(Dispatchers.IO) {
            func.invoke()
        }
    }
//    private suspend fun saveActive(){
//        list.forEach {
//            if(!it.isActive) {
//                val victorina = victorinaRepo.findById(it.id!!).get()
//                victorina.isActive = true
//                victorinaRepo.save(victorina)
//            }
//        }
//    }

}