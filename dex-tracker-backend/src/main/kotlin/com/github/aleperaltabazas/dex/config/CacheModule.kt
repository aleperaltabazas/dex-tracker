package com.github.aleperaltabazas.dex.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.cache.pokedex.PokedexCache
import com.github.aleperaltabazas.dex.storage.Storage
import com.github.aleperaltabazas.dex.utils.FileSystemHelper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class CacheModule {
    @Bean
    open fun gamePokedexCache(
        fileSystemHelper: FileSystemHelper,
        @Qualifier("objectMapperSnakeCase") objectMapperSnakeCase: ObjectMapper,
        storage: Storage,
    ): PokedexCache = PokedexCache(
        fileSystemHelper = fileSystemHelper,
        objectMapper = objectMapperSnakeCase,
        storage = storage,
    )
}