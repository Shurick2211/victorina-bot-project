package com.nimko.bot.services

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class FrontRequestService {

    fun getRequest():ResponseEntity<String>{
        return ResponseEntity.ok("Hello victorina-bot!")
    }

    fun createVictorina(victorina:String):ResponseEntity<Any>{
        println(victorina)
        return ResponseEntity.ok().build()
    }
}