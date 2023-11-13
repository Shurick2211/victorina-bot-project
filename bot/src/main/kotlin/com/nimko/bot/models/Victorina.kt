package com.nimko.bot.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("victorinas")
data class Victorina(
    @Id
    val id:String? = null,
    val value:String

) {
}