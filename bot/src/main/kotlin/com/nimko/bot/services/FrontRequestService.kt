package com.nimko.bot.services

import com.nimko.bot.models.Person
import com.nimko.bot.models.PersonDto
import com.nimko.bot.models.VictorinaDto
import com.nimko.bot.repositories.PersonRepo
import com.nimko.bot.repositories.VictorinaRepo
import com.nimko.bot.utils.PersonRole
import com.nimko.messageservices.services.MessageServicesListener
import com.nimko.messageservices.telegram.models.message.TextMessage
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
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

    fun putPerson(id: String, person: PersonDto):ResponseEntity<Any>{
        val personById = personsDb.findById(id).get()
        val personForSave =
        if (id == person.id ) {
            personById
        } else
        if (personById.role == PersonRole.ADMIN) {
             personsDb.findById(person.id!!).get()
        } else return ResponseEntity.badRequest().build()
        personForSave.password = person.password!!
        personForSave.role = person.role!!
        personsDb.save(personForSave)
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

    fun getPersons(page:Int , perPage:Int ,  header: String): ResponseEntity<List<PersonDto>> {
        if(personsDb.findById(header).get().role == PersonRole.ADMIN) {
            log.info("page:${page}, size:${perPage}")
            val pageable: Pageable = PageRequest.of(page, perPage, Sort.unsorted())
            val pageList = personsDb.findAll(pageable).content.map { it.toDto() }
            return ResponseEntity.ok().body(pageList)
        }
        return  ResponseEntity.ok().body(emptyList())
    }
}