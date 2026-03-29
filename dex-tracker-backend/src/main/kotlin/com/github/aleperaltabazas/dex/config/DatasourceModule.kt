package com.github.aleperaltabazas.dex.config

import com.github.aleperaltabazas.dex.datasource.google.GoogleOAuthValidator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class DatasourceModule {
    @Bean
    open fun googleValidator() = GoogleOAuthValidator()
}
