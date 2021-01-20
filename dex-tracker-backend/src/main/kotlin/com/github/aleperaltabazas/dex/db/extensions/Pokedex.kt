package com.github.aleperaltabazas.dex.db.extensions

import com.github.aleperaltabazas.dex.db.schema.DexPokemonTable
import com.github.aleperaltabazas.dex.db.schema.PokedexTable
import com.github.aleperaltabazas.dex.model.UserDex
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insert

fun PokedexTable.insert(userId: Long, pokedex: List<UserDex>) = batchInsert(pokedex) { p ->
    this[game] = p.game
    this[PokedexTable.type] = p.type.name
    this[region] = p.region
    this[PokedexTable.userId] = userId
}.also { rows ->
    rows.forEachIndexed { index, row -> DexPokemonTable.insert(row[id].value, pokedex[index].pokemon) }
}

fun PokedexTable.insert(userId: Long, pokedex: UserDex) = this.insert { row ->
    row[this.game] = pokedex.game
    row[this.region] = pokedex.game
    row[this.type] = pokedex.type.name
    row[this.userId] = userId
}.also {
    val id = it[this.id].value
    DexPokemonTable.insert(id, pokedex.pokemon)
}
