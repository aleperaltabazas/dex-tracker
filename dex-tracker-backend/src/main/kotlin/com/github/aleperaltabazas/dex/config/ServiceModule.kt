package com.github.aleperaltabazas.dex.config

import com.github.aleperaltabazas.dex.cache.pokedex.RegionalPokedexCache
import com.github.aleperaltabazas.dex.service.PokemonService
import com.github.aleperaltabazas.dex.storage.PokemonStorage
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named

class ServiceModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("pokemonService")
    fun pokemonService(
        @Named("regionalPokedexCache") regionalPokedexCache: RegionalPokedexCache,
        @Named("pokemonStorage") pokemonStorage: PokemonStorage
    ) = PokemonService(
        regionalPokedexCache = regionalPokedexCache,
        pokemonStorage = pokemonStorage
    )
}