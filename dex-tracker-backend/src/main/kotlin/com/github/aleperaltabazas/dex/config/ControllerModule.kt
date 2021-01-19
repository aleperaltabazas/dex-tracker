package com.github.aleperaltabazas.dex.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.controller.*
import com.github.aleperaltabazas.dex.service.PokemonService
import com.github.aleperaltabazas.dex.service.UsersService
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import spark.template.velocity.VelocityTemplateEngine

class ControllerModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("pokedexController")
    fun pokedexController(
        @Named("objectMapperCamelCase") objectMapper: ObjectMapper,
        @Named("pokemonService") pokemonService: PokemonService,
    ) = PokedexController(
        objectMapper = objectMapper,
        pokemonService = pokemonService,
    )

    @Provides
    @Singleton
    @Named("usersController")
    fun usersController(
        @Named("objectMapperCamelCase") objectMapper: ObjectMapper,
        @Named("usersService") usersService: UsersService,
    ) = UsersController(
        objectMapper = objectMapper,
        usersService = usersService,
    )

    @Provides
    @Singleton
    @Named("frontendController")
    fun frontendController(
        @Named("usersService") usersService: UsersService
    ) = FrontendController(
        usersService = usersService,
        templateEngine = VelocityTemplateEngine(),
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