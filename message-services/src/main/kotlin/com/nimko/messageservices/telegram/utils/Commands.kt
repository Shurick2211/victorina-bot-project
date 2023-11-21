package com.nimko.messageservices.telegram.utils

enum class Commands(private val command: String) {
    START ("/start"),
    CREATOR ("for creator");

    fun getCommand() = command
}