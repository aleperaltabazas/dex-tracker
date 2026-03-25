package com.github.aleperaltabazas.dex.config

import com.github.aleperaltabazas.dex.utils.Environment
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class EnvironmentModule {
    @Bean
    open fun appEnvironment(@Value("\${app.env}") env: String): Environment =
        Environment.valueOf(env.uppercase())
}