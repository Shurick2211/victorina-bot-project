package com.nimko.bot.services

import com.nimko.bot.models.Victorina
import com.nimko.bot.repositories.VictorinaRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class VictorinaServicesImpl @Autowired constructor(
    val victorinaRepo: VictorinaRepo
):VictorinaServices {

    var date:LocalDateTime? = null
    lateinit var listActiveVictorins: List<Victorina>
    override fun getActiveVictorin(): List<Victorina> {
        val today = LocalDateTime.now()
        if (date == null || today.isAfter(date)) getActiveVictorinsFromDb()
        return listActiveVictorins
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

    private fun getActiveVictorinsFromDb() {
       val now = LocalDateTime.now()
       now.withHour(0)
       now.withMinute(0)
       now.withSecond(0)
       date = now.plusDays(1)
       listActiveVictorins = victorinaRepo.findAll().map {it.toVictorina()}.filter {
            it.endDate.isAfter(date)
        }.toList()
    }

}