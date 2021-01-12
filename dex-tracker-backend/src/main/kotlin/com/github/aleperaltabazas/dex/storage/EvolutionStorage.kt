package com.github.aleperaltabazas.dex.storage

import arrow.core.Either
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.aleperaltabazas.dex.db.model.Evolution
import com.github.aleperaltabazas.dex.db.schema.Evolutions
import com.github.aleperaltabazas.dex.extension.catchBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class EvolutionStorage(
    private val db: Database,
    private val objectMapper: ObjectMapper,
) {
    fun findForPokemon(pokemonId: Long): Either<Throwable, List<Evolution>> = Either.catchBlocking {
        transaction(db) {
            Evolutions.select { Evolutions.pokemonId eq pokemonId }
                .asSequence()
                .map(this@EvolutionStorage::toDomain)
                .toList()
        }
    }

    fun save(evolution: Evolution, pokemonId: Long): Either<Throwable, Long> = Either.catchBlocking {
        transaction(db) {
            Evolutions.insert { row ->
                row[this.pokemonId] = pokemonId
                row[name] = evolution.name
                row[method] = objectMapper.writeValueAsString(evolution.method)
            }
        } get Evolutions.id
    }

    private fun toDomain(row: ResultRow) = Evolution(
        id = row[Evolutions.id],
        name = row[Evolutions.name],
        method = objectMapper.readValue(row[Evolutions.method]),
    )
}