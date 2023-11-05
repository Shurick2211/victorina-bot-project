package com.nimko.bot.controllers

import com.nimko.bot.services.FrontRequestService
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.reflect.jvm.internal.impl.metadata.ProtoBuf.Type.Argument.Projection

@RestController
@RequestMapping("/frontrequests")
@OpenAPIDefinition(info =
Info(
    title =  "Victorina-bot",
    version = "0.0.1",
    description = "API for Victorina-bot",
    contact = Contact(name = "Olexandr Nimko",
        email = "shurick2211@gmail.com",
        url = "https://github.com/Shurick2211/")
)
)
@Tag(name = "REST Controller",description = "My Victorina-bot Controller")
class MainController @Autowired constructor(val service:FrontRequestService){

    @GetMapping
    @Operation(
        summary = "Test RestAPI"
    )
    fun getRequest() = service.getRequest()

}