package com.github.aleperaltabazas.dex.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.controller.ExceptionController
import com.github.aleperaltabazas.dex.controller.MiscController
import com.github.aleperaltabazas.dex.controller.PokedexController
import com.github.aleperaltabazas.dex.service.PokemonService
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named

class ControllerModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("pokedexController")
    fun pokedexController(
        @Named("objectMapperCamelCase") objectMapper: ObjectMapper,
        @Named("pokemonService") pokemonService: PokemonService,
    ) = PokedexController(
        objectMapper = objectMapper,
        pokemonService = pokemonService
    )

    @Provides
    @Singleton
    @Named("exceptionController")
    fun exceptionController(
        @Named("objectMapperCamelCase") objectMapper: ObjectMapper,
    ) = ExceptionController(
        objectMapper = objectMapper,
    )

    @Provides
    @Singleton
    @Named("miscController")
    fun miscController() = MiscController()
}