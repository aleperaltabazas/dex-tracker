package com.github.aleperaltabazas.dex.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.cache.pokedex.RegionalPokedexCache
import com.github.aleperaltabazas.dex.connector.RestConnector
import com.github.aleperaltabazas.dex.model.Game
import com.github.aleperaltabazas.dex.utils.FileSystemHelper
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import com.typesafe.config.Config

open class CacheModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("regionalPokedexCache")
    fun regionalPokedexCache(
        @Named("pokeapiConnector") pokeapiConnector: RestConnector,
        @Named("fileSystemHelper") fileSystemHelper: FileSystemHelper,
        @Named("objectMapperSnakeCase") objectMapper: ObjectMapper,
        config: Config,
    ) = RegionalPokedexCache(
        fileSystemHelper = fileSystemHelper,
        objectMapper = objectMapper,
        pokeapiConnector = pokeapiConnector,
        games = config.getConfigList("pokedex.games")
            .map { Game(it) }
    )
}