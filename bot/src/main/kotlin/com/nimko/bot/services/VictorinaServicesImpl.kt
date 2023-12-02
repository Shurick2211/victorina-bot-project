package com.nimko.bot.services

import com.nimko.bot.models.Victorina
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

    lateinit var list:List<Victorina>
    override fun getActiveVictorin(): List<Victorina> {
        val today = LocalDateTime.now()
        list = victorinaRepo.findAll().map { it.toVictorina() }.filter {
            it.endDate.isAfter(today) && it.startDate.isBefore(today)
        }.toList()
        runBackgroundSaveState()
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



    override fun getVictorinaById(id: String): Victorina {
        return list.first{ it.id == id }
    }


    fun runBackgroundSaveState() = runBlocking {
        launch(Dispatchers.IO) {
            saveActive()
        }
    }
    private suspend fun saveActive(){
        list.forEach {
            if(!it.isActive) {
                val victorina = victorinaRepo.findById(it.id!!).get()
                victorina.isActive = true
                victorinaRepo.save(victorina)
            }
        }
    }

}