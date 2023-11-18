package com.nimko.bot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication(scanBasePackages = ["com.nimko"])
class BotApplication

fun main(args: Array<String>) {
    runApplication<BotApplication>(*args)
}

