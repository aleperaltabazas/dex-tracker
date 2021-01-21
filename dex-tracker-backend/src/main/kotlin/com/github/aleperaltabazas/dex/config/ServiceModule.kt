package com.github.aleperaltabazas.dex.config

import com.github.aleperaltabazas.dex.cache.pokedex.GamePokedexCache
import com.github.aleperaltabazas.dex.service.PokemonService
import com.github.aleperaltabazas.dex.service.UsersService
import com.github.aleperaltabazas.dex.storage.Storage
import com.github.aleperaltabazas.dex.utils.HashHelper
import com.github.aleperaltabazas.dex.utils.IdGenerator
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named

class ServiceModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("pokemonService")
    fun pokemonService(
        @Named("gamePokedexCache") gamePokedexCache: GamePokedexCache,
        @Named("storage") storage: Storage
    ) = PokemonService(
        gamePokedexCache = gamePokedexCache,
        storage = storage,
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
}