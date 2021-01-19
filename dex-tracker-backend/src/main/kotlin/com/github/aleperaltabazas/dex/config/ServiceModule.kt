package com.github.aleperaltabazas.dex.config

import com.github.aleperaltabazas.dex.cache.pokedex.GamePokedexCache
import com.github.aleperaltabazas.dex.service.PokemonService
import com.github.aleperaltabazas.dex.service.UsersService
import com.github.aleperaltabazas.dex.storage.PokemonStorage
import com.github.aleperaltabazas.dex.storage.UsersStorage
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
        @Named("pokemonStorage") pokemonStorage: PokemonStorage
    ) = PokemonService(
        gamePokedexCache = gamePokedexCache,
        pokemonStorage = pokemonStorage
    )

    @Provides
    @Singleton
    @Named("usersService")
    fun usersService(
        @Named("usersStorage") usersStorage: UsersStorage,
    ) = UsersService(usersStorage)
}