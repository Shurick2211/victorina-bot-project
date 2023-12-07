package com.nimko.bot.controllers

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

    @PostMapping
    @Operation(
        summary = "Get person by id"
    )
    fun getPerson(@RequestBody id:String) = service.getPerson(id)

    @PutMapping
    @Operation(
        summary = "Save person's password"
    )
    fun putPasswordForPerson (@RequestBody pass:String, request:HttpServletRequest) =
        service.putPasswordForPerson(request.getHeader("id"),pass)



    @GetMapping("/bot")
    fun redirectToBot (@RequestParam("user") userId:String, response:HttpServletResponse){
        service.addPersonOnVictorin(userId)
        response.sendRedirect("https://t.me/${botName}")
    }
}