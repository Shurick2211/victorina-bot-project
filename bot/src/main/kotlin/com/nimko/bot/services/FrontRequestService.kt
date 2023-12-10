package com.nimko.bot.services

import com.nimko.bot.models.VictorinaDto
import com.nimko.bot.repositories.PersonRepo
import com.nimko.bot.repositories.VictorinaRepo
import com.nimko.bot.utils.PersonRole
import com.nimko.messageservices.services.MessageServicesListener
import com.nimko.messageservices.services.MessageServicesSender
import com.nimko.messageservices.telegram.models.message.TextMessage
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class FrontRequestService  @Autowired constructor(
    private val victorinasDb: VictorinaRepo,
    private val personsDb: PersonRepo,
    private val messageService:MessageServicesListener
    ) {
    val log = LoggerFactory.getLogger("FRONT_SERV")

    fun getRequest(id: String?):ResponseEntity<Any>{
        val list:List<VictorinaDto> =
            if(id.isNullOrBlank())
                victorinasDb.findAll()
            else victorinasDb.findAllByOwnerId(id)
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

    fun getPerson(id: String, headId:String): ResponseEntity<Any>{
        val person = personsDb.findById(id).get()
        if (id != headId || headId == PersonRole.ADMIN.name) person.password = "***"
        return ResponseEntity.ok(person.toDto())
    }

    fun putPasswordForPerson(id: String, password:String):ResponseEntity<Any>{
        val person = personsDb.findById(id).get()
        person.password = password
        personsDb.save(person)
        return ResponseEntity.ok().build()
    }

    fun addPersonOnVictorin(userId:String){
        println("FROM REDIRECT" + userId)
    }

    fun getMessageFront(message:String, userId:String):ResponseEntity<Any>{
        log.info(userId+": "+message)
        val person = personsDb.findById(userId).get()
        if(person.role != PersonRole.ADMIN) {
            val admins = personsDb.findAllByRole(PersonRole.ADMIN)
            admins.forEach {
                messageService.onSender().sendText(TextMessage(it.id,
                    "${person.id}/@${person.userName}: ${message} \n\n" +
                            "Затисни повідомлення, а після натисни \"Відповісти\"", null))
            }
        }

        return ResponseEntity.ok().build()
    }
}