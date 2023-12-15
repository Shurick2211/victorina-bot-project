package com.nimko.bot.controllers

import com.nimko.bot.services.FrontRequestService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/others")
@Tag(name = "REST Controller",description = "My Controller for others requests")
class OthersController @Autowired constructor(val service: FrontRequestService){

    @GetMapping("/channels")
    @Operation(
        summary = "Get All channels where bot is admin from Db"
    )
    fun getAllChannels(request: HttpServletRequest) = service.getChannels(request.getHeader("id"))

    @GetMapping("/message")
    @Operation(
        summary = "Messaging front"
    )
    fun getMessageFront (@RequestParam("mess") mess:String, @RequestParam("receiverId") receiverId:String?, request:HttpServletRequest) =
        service.getMessageFront(mess, request.getHeader("id"), receiverId)
}