package com.github.aleperaltabazas.dex.storage

import com.github.aleperaltabazas.dex.db.Pokemon
import com.github.aleperaltabazas.dex.db.schema.PokemonsTable
import com.github.aleperaltabazas.dex.db.reifying
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class PokemonStorage(
    private val db: Database
) {
    fun findAll(): List<Pokemon> = transaction(db) {
        PokemonsTable.reifying { selectAll() }.toList()
    }

    fun find(dexNumbers: List<Int>): List<Pokemon> = transaction(db) {
        PokemonsTable.reifying { select { PokemonsTable.nationalDexNumber inList dexNumbers } }
    }.toList()
}