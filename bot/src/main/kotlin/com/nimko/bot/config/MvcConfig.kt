package com.nimko.bot.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import java.util.*

@Configuration
class MvcConfig:WebMvcConfigurer {
    @Bean
    fun localeResolver(): LocaleResolver {
        val slr = SessionLocaleResolver()
        slr.setDefaultLocale(Locale.US)
        return slr
    }

    @Bean
    fun localeInterceptor(): LocaleChangeInterceptor {
        val localeInterceptor = LocaleChangeInterceptor()
        localeInterceptor.paramName = "lang"
        return localeInterceptor
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(localeInterceptor())
    }

    //for inline angular-routing
    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/start").setViewName("forward:/index.html")
        registry.addViewController("/list").setViewName("forward:/index.html")
        registry.addViewController("/create").setViewName("forward:/index.html")
        registry.addViewController("/edit/**").setViewName("forward:/index.html");
    }
}