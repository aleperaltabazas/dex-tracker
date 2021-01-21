package com.github.aleperaltabazas.dex.config

import com.github.aleperaltabazas.dex.cache.pokedex.RegionalPokedexCache
import com.github.aleperaltabazas.dex.model.Game
import com.github.aleperaltabazas.dex.service.GameService
import com.github.aleperaltabazas.dex.service.PokemonService
import com.github.aleperaltabazas.dex.service.UsersService
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
    @Named("pokemonService")
    fun pokemonService(
        @Named("gameService") gameService: GameService,
        @Named("gamePokedexCache") regionalPokedexCache: RegionalPokedexCache,
        @Named("storage") storage: Storage
    ) = PokemonService(
        regionalPokedexCache = regionalPokedexCache,
        storage = storage,
        gameService = gameService
    )

    @Provides
    @Singleton
    @Named("usersService")
    fun usersService(
        @Named("storage") storage: Storage,
        @Named("pokemonService") pokemonService: PokemonService,
        @Named("idGenerator") idGenerator: IdGenerator,
        @Named("hash") hashHelper: HashHelper,
    ) = UsersService(
        storage = storage,
        pokemonService = pokemonService,
        idGenerator = idGenerator,
        hash = hashHelper
    )

    @Provides
    @Singleton
    @Named("gameService")
    fun gameService(config: Config) = GameService(
        games = config.getConfigList("pokedex.games").map { Game(it) }
    )
}