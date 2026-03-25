package com.github.aleperaltabazas.dex.config

import com.github.aleperaltabazas.dex.datasource.google.GoogleOAuthValidator
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class DatasourceModule {
    @Bean
    open fun googleValidator(
        @Value("\${app.google.client-id}") clientId: String,
    ) = GoogleOAuthValidator(
        GoogleIdTokenVerifier.Builder(
            GoogleNetHttpTransport.newTrustedTransport(),
            GsonFactory.getDefaultInstance(),
        )
            .setAudience(listOf(clientId))
            .build()
    )
}