package com.github.aleperaltabazas.dex.storage

import arrow.core.Either
import com.github.aleperaltabazas.dex.db.model.Stats
import com.github.aleperaltabazas.dex.db.schema.Statses
import com.github.aleperaltabazas.dex.extension.catchBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class StatsStorage(
    private val db: Database
) {
    fun find(statsId: Long): Either<Throwable, Stats> = Either.catchBlocking {
        Statses.select { Statses.id eq statsId }
            .limit(1)
            .firstOrNull()
            ?.let { this.toDomain(it) }
            ?: throw RuntimeException("")
    }

    fun save(stats: Stats): Either<Throwable, Long> = Either.catchBlocking {
        transaction(db) {
            Statses.insert { row ->
                row[hp] = stats.hp
                row[attack] = stats.attack
                row[defense] = stats.defense
                row[specialAttack] = stats.specialAttack
                row[specialDefense] = stats.specialDefense
                row[speed] = stats.speed
            } get Statses.id
        }
    }

    private fun toDomain(row: ResultRow): Stats = Stats(
        id = row[Statses.id],
        hp = row[Statses.hp],
        attack = row[Statses.attack],
        defense = row[Statses.defense],
        specialAttack = row[Statses.specialAttack],
        specialDefense = row[Statses.specialDefense],
        speed = row[Statses.speed],
    )
}