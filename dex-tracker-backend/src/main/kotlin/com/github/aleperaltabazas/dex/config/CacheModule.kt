package com.github.aleperaltabazas.dex.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.cache.pokedex.PokedexCache
import com.github.aleperaltabazas.dex.storage.Storage
import com.github.aleperaltabazas.dex.utils.FileSystemHelper
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named

open class CacheModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("gamePokedexCache")
    fun gamePokedexCache(
        @Named("fileSystemHelper") fileSystemHelper: FileSystemHelper,
        @Named("objectMapperSnakeCase") objectMapper: ObjectMapper,
        @Named("storage") storage: Storage,
    ) = PokedexCache(
        fileSystemHelper = fileSystemHelper,
        objectMapper = objectMapper,
        storage = storage,
    )
}