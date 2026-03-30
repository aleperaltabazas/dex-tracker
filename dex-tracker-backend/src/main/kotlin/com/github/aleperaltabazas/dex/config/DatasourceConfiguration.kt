package com.github.aleperaltabazas.dex.config

import com.github.aleperaltabazas.dex.datasource.google.GoogleOAuthValidator
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DatasourceConfiguration(
    @Value("\${google.client-id}") private val clientId: String,
    @Value("\${google.client-secret}") private val clientSecret: String,
) {
    @Bean
    fun googleOAuthValidator() = GoogleOAuthValidator(clientId = clientId, clientSecret = clientSecret)
}
