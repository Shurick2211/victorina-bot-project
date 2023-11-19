package com.nimko.messageservices.models.message

data class MenuMessage(
    val userId:String,
    val title:String,
    val buttonNames:List<String>
)
