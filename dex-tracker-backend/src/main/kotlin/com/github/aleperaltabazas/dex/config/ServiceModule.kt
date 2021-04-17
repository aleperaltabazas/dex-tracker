package com.github.aleperaltabazas.dex.config

import com.github.aleperaltabazas.dex.cache.pokedex.PokedexCache
import com.github.aleperaltabazas.dex.service.LoginService
import com.github.aleperaltabazas.dex.service.PokedexService
import com.github.aleperaltabazas.dex.service.SessionService
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
    @Named("pokedexService")
    fun pokedexService(
        @Named("gamePokedexCache") pokedexCache: PokedexCache,
        @Named("storage") storage: Storage,
        @Named("idGenerator") idGenerator: IdGenerator,
    ) = PokedexService(
        pokedexCache = pokedexCache,
        storage = storage,
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
    @Named("loginService")
    fun loginService(
        @Named("storage") storage: Storage,
        @Named("usersService") usersService: UsersService,
        @Named("sessionService") sessionService: SessionService,
    ) = LoginService(
        usersService = usersService,
        sessionService = sessionService,
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