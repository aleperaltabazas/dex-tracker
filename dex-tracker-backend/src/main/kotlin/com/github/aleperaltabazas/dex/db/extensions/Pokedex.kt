package com.github.aleperaltabazas.dex.db.extensions

import com.github.aleperaltabazas.dex.db.schema.DexPokemonTable
import com.github.aleperaltabazas.dex.db.schema.PokedexTable
import com.github.aleperaltabazas.dex.model.UserDex
import org.jetbrains.exposed.sql.batchInsert

fun PokedexTable.insert(userId: Long, pokedex: List<UserDex>) = batchInsert(pokedex) { p ->
    this[game] = p.game
    this[PokedexTable.type] = p.type.name
    this[region] = p.region
    this[PokedexTable.userId] = userId
}.also { rows ->
    rows.forEachIndexed { index, row -> DexPokemonTable.insert(row[id].value, pokedex[index].pokemon) }
}
