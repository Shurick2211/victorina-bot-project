package com.nimko.bot.services

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class FrontRequestService {

    fun getRequest():ResponseEntity<String>{
        return ResponseEntity.ok("Hello victorina-bot!")
    }
}