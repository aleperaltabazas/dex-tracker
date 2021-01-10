package com.github.aleperaltabazas.dex.storage

import arrow.core.Either
import com.github.aleperaltabazas.dex.db.model.Pokemon
import com.github.aleperaltabazas.dex.db.reifying
import com.github.aleperaltabazas.dex.db.schema.PokemonsTable
import com.github.aleperaltabazas.dex.extension.catchBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class PokemonStorage(
    private val db: Database
) {
    fun findAll(): Either<Throwable, List<Pokemon>> = Either.catchBlocking {
        transaction(db) {
            PokemonsTable.reifying { selectAll() }.toList()
        }
    }

    fun find(dexNumbers: List<Int>): Either<Throwable, List<Pokemon>> = Either.catchBlocking {
        transaction(db) {
            PokemonsTable.reifying { select { PokemonsTable.nationalDexNumber inList dexNumbers } }
        }.toList()
    }
}