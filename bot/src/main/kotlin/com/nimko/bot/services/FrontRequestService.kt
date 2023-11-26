package com.nimko.bot.services

import com.nimko.bot.models.VictorinaDto
import com.nimko.bot.repositories.PersonRepo
import com.nimko.bot.repositories.VictorinaRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class FrontRequestService  @Autowired constructor(
    val victorinasDb: VictorinaRepo,
    val personsDb: PersonRepo
    ) {


    fun getRequest():ResponseEntity<Any>{
        val list:List<VictorinaDto> = victorinasDb.findAll()
        return ResponseEntity.ok(list)
    }

    fun createVictorina(victorinaDto:VictorinaDto):ResponseEntity<Any>{
        victorinasDb.save(victorinaDto)
        return ResponseEntity.ok().build()
    }

    fun deleteVictorina(id: String): ResponseEntity<Any> {
        victorinasDb.deleteById(id)
        return ResponseEntity.ok().build()
    }

    fun getPerson(id: String): ResponseEntity<Any>{
        val person = personsDb.findById(id).get()
        return ResponseEntity.ok(person.toDto())
    }

    fun putPasswordForPerson(id: String, password:String):ResponseEntity<Any>{
        val person = personsDb.findById(id).get()
        person.password = password
        personsDb.save(person)
        return ResponseEntity.ok().build()
    }

}