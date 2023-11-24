package com.nimko.bot.controllers

import com.nimko.bot.services.FrontRequestService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/persons")
@Tag(name = "REST Controller",description = "My Persons Controller")
class PersonController @Autowired constructor(val service: FrontRequestService) {

    @PostMapping
    @Operation(
        summary = "Get person by id"
    )
    fun getPerson(@RequestBody id:String) = service.getPerson(id)

    @PutMapping
    @Operation(
        summary = "Save person's password"
    )
    fun putPasswordForPerson(@RequestParam("id") id:String, @RequestBody pass:String) =
        service.putPasswordForPerson(id,pass)

}