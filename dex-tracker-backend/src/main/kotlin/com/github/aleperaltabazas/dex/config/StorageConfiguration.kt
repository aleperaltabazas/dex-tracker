package com.github.aleperaltabazas.dex.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.storage.Storage
import com.mongodb.client.MongoDatabase
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StorageConfiguration {
    @Bean
    fun storage(db: MongoDatabase, @Qualifier("snakeCaseObjectMapper") objectMapper: ObjectMapper): Storage =
        Storage(db = db, objectMapper = objectMapper)
}
