package com.nimko.bot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling


@SpringBootApplication(scanBasePackages = ["com.nimko"])
@EnableScheduling
class BotApplication

fun main(args: Array<String>) {
    runApplication<BotApplication>(*args)
}

