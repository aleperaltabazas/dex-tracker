package com.github.aleperaltabazas.dex.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.storage.EvolutionStorage
import com.github.aleperaltabazas.dex.storage.FormStorage
import com.github.aleperaltabazas.dex.storage.PokemonStorage
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import org.jetbrains.exposed.sql.Database

class StorageModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("evolutionStorage")
    fun evolutionStorage(
        @Named("objectMapperSnakeCase") objectMapper: ObjectMapper,
        @Named("db") db: Database
    ) = EvolutionStorage(objectMapper, db)

    @Provides
    @Singleton
    @Named("formStorage")
    fun formStorage(
        @Named("db") db: Database
    ) = FormStorage(db)

    @Provides
    @Singleton
    @Named("pokemonStorage")
    fun pokemonStorage(
        @Named("evolutionStorage") evolutionStorage: EvolutionStorage,
        @Named("formStorage") formStorage: FormStorage,
        @Named("db") db: Database
    ) = PokemonStorage(
        evolutionStorage,
        formStorage,
        db
    )
}
