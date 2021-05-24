package com.github.aleperaltabazas.dex.config

import com.github.aleperaltabazas.dex.datasource.google.GoogleOAuthValidator
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import com.typesafe.config.Config

class DatasourceModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("googleValidator")
    fun googleValidator(config: Config) = GoogleOAuthValidator(
        GoogleIdTokenVerifier.Builder(
            GoogleNetHttpTransport.newTrustedTransport(),
            GsonFactory.getDefaultInstance(),
        )
            .setAudience(listOf(config.getString("google.client-id")))
            .build()
    )
}