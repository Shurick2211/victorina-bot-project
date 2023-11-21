package com.nimko.messageservices.telegram.models.message

data class MenuMessage(
    val userId:String,
    val title:String,
    val buttonNames:List<String>
)
