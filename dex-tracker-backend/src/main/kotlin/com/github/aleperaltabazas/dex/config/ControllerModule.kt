package com.github.aleperaltabazas.dex.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.controller.*
import com.github.aleperaltabazas.dex.env.Env
import com.github.aleperaltabazas.dex.mapper.ModelMapper
import com.github.aleperaltabazas.dex.service.GameService
import com.github.aleperaltabazas.dex.service.PokemonService
import com.github.aleperaltabazas.dex.service.UsersService
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import com.typesafe.config.Config
import spark.ModelAndView
import spark.TemplateEngine
import java.io.File

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
        @Named("modelMapper") modelMapper: ModelMapper,
    ) = UsersController(
        objectMapper = objectMapper,
        usersService = usersService,
        modelMapper = modelMapper
    )

    @Provides
    @Singleton
    @Named("frontendController")
    fun frontendController(
        @Named("usersService") usersService: UsersService
    ) = FrontendController(
        usersService = usersService,
        templateEngine = object : TemplateEngine() {
            override fun render(modelAndView: ModelAndView?): String = this.javaClass.getResource("/static/index.html")
                .let { File(it.path) }.readText(Charsets.UTF_8)
        },
    )

    @Provides
    @Singleton
    @Named("exceptionController")
    fun exceptionController(
        @Named("objectMapperCamelCase") objectMapper: ObjectMapper,
        @Named("env") env: Env,
        config: Config
    ) = ExceptionController(
        objectMapper = objectMapper,
        env = env,
        config.getStringList("cors.origins")
    )

    @Provides
    @Singleton
    @Named("miscController")
    fun miscController(
        @Named("env") env: Env,
        config: Config
    ) = MiscController(
        env = env,
        corsOrigins = config.getStringList("cors.origins")
    )

    @Provides
    @Singleton
    @Named("gameController")
    fun gameController(
        @Named("gameService") gameService: GameService,
        @Named("objectMapperCamelCase") objectMapper: ObjectMapper
    ) = GameController(
        gameService = gameService,
        objectMapper = objectMapper,
    )
}