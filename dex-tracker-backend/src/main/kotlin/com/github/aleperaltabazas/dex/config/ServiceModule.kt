package com.github.aleperaltabazas.dex.config

import com.github.aleperaltabazas.dex.cache.pokedex.RegionalPokedexCache
import com.github.aleperaltabazas.dex.model.Game
import com.github.aleperaltabazas.dex.service.*
import com.github.aleperaltabazas.dex.storage.Storage
import com.github.aleperaltabazas.dex.utils.HashHelper
import com.github.aleperaltabazas.dex.utils.IdGenerator
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import com.typesafe.config.Config

class ServiceModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("pokedexService")
    fun pokedexService(
        @Named("gameService") gameService: GameService,
        @Named("gamePokedexCache") regionalPokedexCache: RegionalPokedexCache,
        @Named("storage") storage: Storage,
        @Named("idGenerator") idGenerator: IdGenerator,
    ) = PokedexService(
        regionalPokedexCache = regionalPokedexCache,
        storage = storage,
        gameService = gameService,
        idGenerator = idGenerator,
    )

    @Provides
    @Singleton
    @Named("usersService")
    fun usersService(
        @Named("storage") storage: Storage,
        @Named("idGenerator") idGenerator: IdGenerator,
    ) = UsersService(
        storage = storage,
        idGenerator = idGenerator,
    )

    @Provides
    @Singleton
    @Named("gameService")
    fun gameService(config: Config) = GameService(
        games = config.getConfigList("pokedex.games").map { Game(it) }
    )

    @Provides
    @Singleton
    @Named("loginService")
    fun loginService(
        @Named("storage") storage: Storage,
        @Named("usersService") usersService: UsersService,
    ) = LoginService(
        usersService = usersService,
    )

    @Provides
    @Singleton
    @Named("sessionService")
    fun sessionService(
        @Named("storage") storage: Storage,
        @Named("hash") hashHelper: HashHelper,
    ) = SessionService(
        hash = hashHelper,
        storage = storage,
    )
}