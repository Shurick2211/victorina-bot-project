package com.nimko.messageservices.models.message

import com.nimko.messageservices.models.others.InlineButton

data class ChangeInlineMessage(
    val userId:String,
    val buttons:List<InlineButton>
)
