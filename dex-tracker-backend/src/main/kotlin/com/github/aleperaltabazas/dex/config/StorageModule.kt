package com.github.aleperaltabazas.dex.config

import com.github.aleperaltabazas.dex.storage.PokemonStorage
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import org.jetbrains.exposed.sql.Database

class StorageModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("pokemonStorage")
    fun pokemonStorage(
        @Named("db") db: Database
    ) = PokemonStorage(
        db
    )
}
