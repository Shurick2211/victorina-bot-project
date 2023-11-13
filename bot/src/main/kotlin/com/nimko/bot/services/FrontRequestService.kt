package com.nimko.bot.services

import com.nimko.bot.models.Victorina
import com.nimko.bot.repositories.VictorinaRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class FrontRequestService  @Autowired constructor(val victorinasDb: VictorinaRepo) {


    fun getRequest():ResponseEntity<String>{
        return ResponseEntity.ok("Hello victorina-bot!")
    }

    fun createVictorina(victorina:String):ResponseEntity<Any>{
        println(victorina)
        victorinasDb.save(Victorina(value = victorina))
        return ResponseEntity.ok().build()
    }
}