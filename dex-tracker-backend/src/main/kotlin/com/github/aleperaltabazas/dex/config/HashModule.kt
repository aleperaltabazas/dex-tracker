package com.github.aleperaltabazas.dex.config

import com.github.aleperaltabazas.dex.utils.HashHelper
import com.github.aleperaltabazas.dex.utils.IdGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.text.SimpleDateFormat

@Configuration
open class HashModule {
    @Bean
    open fun hash() = HashHelper()

    @Bean
    open fun idGenerator() = IdGenerator(SimpleDateFormat("yyyy-MM-dd"))
}