package com.nimko.messageservices.telegram.models.others

import com.nimko.messageservices.telegram.utils.CallbackData

data class InlineButton(
    val name:String,
    val responseData:CallbackData
)
