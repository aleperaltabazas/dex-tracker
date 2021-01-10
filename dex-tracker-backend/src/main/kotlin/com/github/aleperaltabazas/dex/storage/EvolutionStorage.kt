package com.github.aleperaltabazas.dex.storage

import arrow.core.Either
import com.github.aleperaltabazas.dex.db.model.Evolution
import com.github.aleperaltabazas.dex.db.schema.Evolutions
import com.github.aleperaltabazas.dex.extension.catchBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class EvolutionStorage(
    private val db: Database,
) {
    fun save(evolution: Evolution, pokemonId: Long): Either<Throwable, Long> = Either.catchBlocking {
        transaction(db) {
            Evolutions.insert { row ->
                row[this.pokemonId] = pokemonId
                row[name] = evolution.name
                row[method] = evolution.method.serialize()
            }
        } get Evolutions.id
    }
}