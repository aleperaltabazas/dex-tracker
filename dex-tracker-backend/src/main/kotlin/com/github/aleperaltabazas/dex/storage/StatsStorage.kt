package com.github.aleperaltabazas.dex.storage

import arrow.core.Either
import com.github.aleperaltabazas.dex.db.model.Stats
import com.github.aleperaltabazas.dex.db.schema.Statses
import com.github.aleperaltabazas.dex.extension.catchBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class StatsStorage(
    private val db: Database
) {
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
}