package com.nimko.messageservices.telegram.models.message

import com.nimko.messageservices.telegram.models.others.InlineButton

data class ChangeInlineMessage(
    val userId:String,
    val messageId:String,
    val buttons:List<InlineButton>
)
