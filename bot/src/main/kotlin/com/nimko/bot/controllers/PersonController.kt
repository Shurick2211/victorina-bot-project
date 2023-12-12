package com.nimko.bot.controllers

import com.nimko.bot.models.Person
import com.nimko.bot.models.PersonDto
import com.nimko.bot.services.FrontRequestService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/persons")
@Tag(name = "REST Controller",description = "My Persons Controller")
class PersonController @Autowired constructor(val service: FrontRequestService) {

    @Value("\${name.telega}")
    lateinit var botName:String

    @GetMapping
    @Operation(
        summary = "Get all persons"
    )
    fun getPersons(@RequestParam("page") page:Int, @RequestParam("perpage") perPage:Int,  request:HttpServletRequest) =
        service.getPersons( page=page, perPage=perPage, header = request.getHeader("id"))

    @PostMapping
    @Operation(
        summary = "Get person by id"
    )
    fun getPerson(@RequestBody id:String, request:HttpServletRequest) =
        service.getPerson(id, request.getHeader("id"))

    @PutMapping
    @Operation(
        summary = "Save person's password"
    )
    fun putPerson (@RequestBody person:PersonDto, request:HttpServletRequest) =
        service.putPerson(request.getHeader("id"),person)



    @GetMapping("/bot")
    @Operation(
        summary = "Redirect to bot"
    )
    fun redirectToBot (@RequestParam("user") userId:String, response:HttpServletResponse){
        service.addPersonOnVictorin(userId)
        response.sendRedirect("https://t.me/${botName}")
    }

    @GetMapping("/message")
    @Operation(
        summary = "Messaging front"
    )
    fun getMessageFront (@RequestParam("mess") mess:String, request:HttpServletRequest) =
        service.getMessageFront(mess, request.getHeader("id"))

}