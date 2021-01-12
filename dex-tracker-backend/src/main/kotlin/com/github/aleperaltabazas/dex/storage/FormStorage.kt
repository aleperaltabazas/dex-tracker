package com.github.aleperaltabazas.dex.storage

import arrow.core.Either
import arrow.core.extensions.fx
import com.github.aleperaltabazas.dex.db.model.Form
import com.github.aleperaltabazas.dex.db.schema.Forms
import com.github.aleperaltabazas.dex.extension.catchBlocking
import com.github.aleperaltabazas.dex.extension.sequence
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class FormStorage(
    private val db: Database,
    private val statsStorage: StatsStorage,
) {
    fun findForPokemon(pokemonId: Long): Either<Throwable, List<Form>> = transaction(db) {
        Either.fx {
            Forms.select { Forms.pokemonId eq pokemonId }
                .asSequence()
                .map(this@FormStorage::toDomain)
                .toList()
                .sequence()
                .bind()
        }
    }

    fun save(form: Form, pokemonId: Long): Either<Throwable, Long> = Either.fx {
        val statsId = form.stats?.let { statsStorage.save(it) }?.bind()

        Either.catchBlocking {
            transaction(db) {
                Forms.insert { row ->
                    row[this.pokemonId] = pokemonId
                    row[name] = form.name
                    row[this.statsId] = statsId
                } get Forms.id
            }
        }.bind()
    }

    private fun toDomain(row: ResultRow): Either<Throwable, Form> = Either.fx {
        Form(
            id = row[Forms.id],
            name = row[Forms.name],
            stats = row[Forms.statsId]?.let { statsStorage.find(it).bind() }
        )
    }
}