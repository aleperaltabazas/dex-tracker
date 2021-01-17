package com.github.aleperaltabazas.dex.storage

import com.github.aleperaltabazas.dex.db.Where
import com.github.aleperaltabazas.dex.db.extensions.selectWhere
import com.github.aleperaltabazas.dex.db.schema.PokemonTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

open class PokemonStorage(
    private val db: Database,
) {
    open fun findOne(where: Where) = transaction(db) {
        PokemonTable.selectWhere(where, limit = 1).firstOrNull()
    }

    open fun findAll(where: Where) = transaction(db) {
        PokemonTable.selectWhere(where)
    }
}