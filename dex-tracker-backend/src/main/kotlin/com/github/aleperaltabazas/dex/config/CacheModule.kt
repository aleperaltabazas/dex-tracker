package com.github.aleperaltabazas.dex.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.cache.pokedex.RegionalPokedexCache
import com.github.aleperaltabazas.dex.connector.RestConnector
import com.github.aleperaltabazas.dex.domain.Game
import com.github.aleperaltabazas.dex.utils.FileSystemHelper
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import com.typesafe.config.Config

open class CacheModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("hgssRegionalPokedexCache")
    open fun hgssRegionalPokedexCache(
        @Named("pokeapiConnector") pokeapiConnector: RestConnector,
        @Named("objectMapperSnakeCase") objectMapper: ObjectMapper,
        fileSystemHelper: FileSystemHelper,
        config: Config
    ) = regionalCache(
        pokeapiConnector,
        objectMapper,
        fileSystemHelper,
        config.getConfig("hgss")
    )

    private fun regionalCache(
        pokeapiConnector: RestConnector,
        objectMapper: ObjectMapper,
        fileSystemHelper: FileSystemHelper,
        gameConfig: Config,
    ) = RegionalPokedexCache(
        objectMapper = objectMapper,
        fileSystemHelper = fileSystemHelper,
        pokeapiConnector = pokeapiConnector,
        game = Game(
            name = gameConfig.getString("name"),
            dexId = gameConfig.getString("id"),
            title = gameConfig.getString("title"),
            fullTitle = gameConfig.getString("full-title"),
            spritePokemon = gameConfig.getString("sprite-pokemon"),
            region = gameConfig.getString("region"),
            type = "regional",
            nationalCutoff = null,
        )
    )
}