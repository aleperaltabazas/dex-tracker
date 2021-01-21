package com.github.aleperaltabazas.dex.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.storage.Storage
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import com.mongodb.client.MongoDatabase

class StorageModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("storage")
    fun storage(
        @Named("objectMapperSnakeCase") objectMapper: ObjectMapper,
        @Named("db") db: MongoDatabase
    ) = Storage(
        db = db,
        objectMapper = objectMapper
    )
}
