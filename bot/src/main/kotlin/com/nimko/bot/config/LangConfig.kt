package com.nimko.bot.config

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource

@Configuration
class LangConfig {

    @Bean
    fun messageSource():MessageSource{
        val messageSource = ResourceBundleMessageSource()
        messageSource.setBasename("classpath:messages")
        messageSource.setDefaultEncoding("UTF-8")
        return messageSource
    }


}