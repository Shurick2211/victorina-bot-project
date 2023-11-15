package com.nimko.bot.services

import com.nimko.bot.models.Victorina
import com.nimko.bot.repositories.VictorinaRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class FrontRequestService  @Autowired constructor(val victorinasDb: VictorinaRepo) {


    fun getRequest():ResponseEntity<Any>{
        val list:List<Victorina> = victorinasDb.findAll()
        return ResponseEntity.ok(list)
    }

    fun createVictorina(victorina:Victorina):ResponseEntity<Any>{
        victorina.id=null
        victorinasDb.save(victorina)
        return ResponseEntity.ok().build()
    }
}