package com.github.aleperaltabazas.dex.config

import com.github.aleperaltabazas.dex.cache.pokedex.PokedexCache
import com.github.aleperaltabazas.dex.datasource.google.GoogleOAuthValidator
import com.github.aleperaltabazas.dex.service.*
import com.github.aleperaltabazas.dex.storage.Storage
import com.github.aleperaltabazas.dex.utils.HashHelper
import com.github.aleperaltabazas.dex.utils.IdGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ServiceModule {
    @Bean
    open fun pokedexService(
        gamePokedexCache: PokedexCache,
        idGenerator: IdGenerator,
    ) = PokedexService(
        pokedexCache = gamePokedexCache,
        idGenerator = idGenerator,
    )

    @Bean
    open fun usersService(
        storage: Storage,
        sessionService: SessionService,
        idGenerator: IdGenerator,
    ) = UsersService(
        storage = storage,
        idGenerator = idGenerator,
        sessionService = sessionService,
    )

    @Bean
    open fun loginService(
        usersService: UsersService,
        sessionService: SessionService,
        googleOAuthValidator: GoogleOAuthValidator,
    ) = LoginService(
        usersService = usersService,
        sessionService = sessionService,
        google = googleOAuthValidator,
    )

    @Bean
    open fun sessionService(
        storage: Storage,
        hash: HashHelper,
    ) = SessionService(
        hash = hash,
        storage = storage,
    )

    @Bean
    open fun pokemonService(
        storage: Storage,
        pokedexService: PokedexService,
    ) = PokemonService(
        pokedexService = pokedexService,
        storage = storage,
    )
}
