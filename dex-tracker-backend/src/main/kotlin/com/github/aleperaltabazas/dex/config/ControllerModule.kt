package com.github.aleperaltabazas.dex.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.controller.*
import com.github.aleperaltabazas.dex.env.Env
import com.github.aleperaltabazas.dex.mapper.ModelMapper
import com.github.aleperaltabazas.dex.service.*
import com.github.aleperaltabazas.dex.utils.HandlebarsTemplateEngineBuilder
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import com.typesafe.config.Config
import spark.template.handlebars.HandlebarsTemplateEngine

class ControllerModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("pokedexController")
    fun pokedexController(
        @Named("objectMapperCamelCase") objectMapper: ObjectMapper,
        @Named("pokedexService") pokedexService: PokedexService,
    ) = PokedexController(
        objectMapper = objectMapper,
        pokedexService = pokedexService,
    )

    @Provides
    @Singleton
    @Named("usersController")
    fun usersController(
        @Named("objectMapperCamelCase") objectMapper: ObjectMapper,
        @Named("usersService") usersService: UsersService,
        @Named("modelMapper") modelMapper: ModelMapper,
        @Named("pokedexService") pokedexService: PokedexService
    ) = UsersController(
        objectMapper = objectMapper,
        usersService = usersService,
        modelMapper = modelMapper,
        pokedexService = pokedexService,
    )

    @Provides
    @Singleton
    @Named("frontendController")
    fun frontendController(
        @Named("usersService") usersService: UsersService,
        @Named("sessionService") sessionService: SessionService,
    ) = FrontendController(
        engine = HandlebarsTemplateEngineBuilder(HandlebarsTemplateEngine())
            .withDefaultHelpers()
            .build(),
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

    @Provides
    @Singleton
    @Named("loginController")
    fun loginController(
        @Named("loginService") loginService: LoginService,
        @Named("objectMapperCamelCase") objectMapper: ObjectMapper,
        @Named("modelMapper") modelMapper: ModelMapper,
        @Named("sessionService") sessionService: SessionService,
    ) = LoginController(
        modelMapper = modelMapper,
        loginService = loginService,
        objectMapper = objectMapper,
        sessionService = sessionService,
    )
}