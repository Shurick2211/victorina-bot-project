package com.nimko.bot.controllers

import com.nimko.bot.models.VictorinaDto
import com.nimko.bot.services.FrontRequestService
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/victorinas")
@OpenAPIDefinition(info =
Info(
    title =  "Victorina-bot",
    version = "0.0.1",
    description = "API for Victorina-bot",
    contact = Contact(name = "Olexandr Nimko",
        email = "shurick2211@gmail.com",
        url = "https://github.com/Shurick2211/victorina-bot-project.git")
)
)
@Tag(name = "REST Controller",description = "My Victorina Controller")
class VictorinaController @Autowired constructor(val service:FrontRequestService){

    @GetMapping
    @Operation(
        summary = "Get All victorins from Db"
    )
    fun allVictorins(@RequestParam("id") id:String?) = service.getRequest(id)

    @PostMapping
    @Operation(
        summary = "Create victorina"
    )
    fun createVictorina(@RequestBody victorinaDto:VictorinaDto ) = service.createVictorina(victorinaDto)

    @DeleteMapping
    @Operation(
        summary = "Delete victorina by id"
    )
    fun deleteVictorina(@RequestBody id:String) = service.deleteVictorina(id)

}