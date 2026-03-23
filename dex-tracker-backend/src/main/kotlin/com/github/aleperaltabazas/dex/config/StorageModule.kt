package com.github.aleperaltabazas.dex.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.storage.Storage
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import com.mongodb.client.MongoDatabase

@Configuration
open class StorageModule {
    @Bean
    open fun storage(
        objectMapperSnakeCase: ObjectMapper,
        db: MongoDatabase,
    ) = Storage(
        db = db,
        objectMapper = objectMapperSnakeCase,
    )
}
