package com.github.aleperaltabazas.dex.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.connector.RestConnector
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import com.typesafe.config.Config

open class ConnectionModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("pokeapiConnector")
    open fun pokeapiConnector(
        config: Config,
        @Named("objectMapperSnakeCase") objectMapper: ObjectMapper,
    ) = RestConnector.create(
        objectMapper = objectMapper,
        moduleConfig = config.getConfig("pokeapi"),
    )
}