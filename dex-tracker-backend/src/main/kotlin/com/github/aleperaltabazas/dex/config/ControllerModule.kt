package com.github.aleperaltabazas.dex.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.controller.*
import com.github.aleperaltabazas.dex.service.*
import com.github.aleperaltabazas.dex.utils.Environment
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import com.typesafe.config.Config
import spark.TemplateEngine

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
        @Named("pokedexService") pokedexService: PokedexService,
        @Named("sessionService") sessionService: SessionService,
    ) = UsersController(
        objectMapper = objectMapper,
        usersService = usersService,
        pokedexService = pokedexService,
        sessionService = sessionService,
    )

    @Provides
    @Singleton
    @Named("frontendController")
    fun frontendController(
        @Named("usersService") usersService: UsersService,
        @Named("sessionService") sessionService: SessionService,
        @Named("templateEngine") templateEngine: TemplateEngine,
    ) = FrontendController(
        engine = templateEngine,
    )

    @Provides
    @Singleton
    @Named("exceptionController")
    fun exceptionController(
        @Named("objectMapperCamelCase") objectMapper: ObjectMapper,
        @Named("env") env: Environment,
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
        @Named("env") env: Environment,
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
        @Named("sessionService") sessionService: SessionService,
    ) = LoginController(
        loginService = loginService,
        objectMapper = objectMapper,
        sessionService = sessionService,
    )
}