package com.github.aleperaltabazas.dex.config

import com.github.aleperaltabazas.dex.utils.FileSystemHelper
import com.github.aleperaltabazas.dex.utils.HashHelper
import com.github.aleperaltabazas.dex.utils.IdGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.text.SimpleDateFormat

@Configuration
class UtilityConfiguration {
    @Bean
    fun hashHelper() = HashHelper()

    @Bean
    fun idGenerator() = IdGenerator(SimpleDateFormat("yyyy-MM-dd"))

    @Bean
    fun fileSystemHelper() = FileSystemHelper()
}
