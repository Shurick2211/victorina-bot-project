package com.nimko.bot.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Victorina(
    @Id
    val id:String

) {
}