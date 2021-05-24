package com.github.aleperaltabazas.dex.config

import com.github.aleperaltabazas.dex.cache.pokedex.PokedexCache
import com.github.aleperaltabazas.dex.datasource.google.GoogleOAuthValidator
import com.github.aleperaltabazas.dex.service.*
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
    @Named("pokedexService")
    fun pokedexService(
        @Named("gamePokedexCache") pokedexCache: PokedexCache,
        @Named("storage") storage: Storage,
        @Named("idGenerator") idGenerator: IdGenerator,
    ) = PokedexService(
        pokedexCache = pokedexCache,
        idGenerator = idGenerator,
    )

    @Provides
    @Singleton
    @Named("usersService")
    fun usersService(
        @Named("storage") storage: Storage,
        @Named("sessionService") sessionService: SessionService,
        @Named("idGenerator") idGenerator: IdGenerator,
    ) = UsersService(
        storage = storage,
        idGenerator = idGenerator,
        sessionService = sessionService,
    )

    @Provides
    @Singleton
    @Named("loginService")
    fun loginService(
        @Named("usersService") usersService: UsersService,
        @Named("sessionService") sessionService: SessionService,
        @Named("googleValidator") googleOAuthValidator: GoogleOAuthValidator,
    ) = LoginService(
        usersService = usersService,
        sessionService = sessionService,
        google = googleOAuthValidator,
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

    @Provides
    @Singleton
    @Named("pokemonService")
    fun pokemonService(
        @Named("storage") storage: Storage,
        @Named("pokedexService") pokedexService: PokedexService
    ) = PokemonService(
        pokedexService = pokedexService,
        storage = storage,
    )
}