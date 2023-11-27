package com.nimko.messageservices.telegram.utils

enum class PollType(private val type: String) {
    QUIZ ("quiz"),
    REGULAR ("regular");

    fun getType() = this.type
}