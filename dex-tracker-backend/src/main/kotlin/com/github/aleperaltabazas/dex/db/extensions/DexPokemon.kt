package com.github.aleperaltabazas.dex.db.extensions

import com.github.aleperaltabazas.dex.db.schema.DexPokemonTable
import com.github.aleperaltabazas.dex.model.UserDexPokemon
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.batchInsert

fun DexPokemonTable.insert(pokedexId: Long, pokemon: List<UserDexPokemon>) = batchInsert(pokemon) { p ->
    this[DexPokemonTable.pokedexId] = pokedexId
    this[name] = p.name
    this[caught] = p.caught
    this[dexNumber] = p.dexNumber
}

fun ResultRow.toUserDexPokemon(): UserDexPokemon = UserDexPokemon(
    name = this[DexPokemonTable.name],
    caught = this[DexPokemonTable.caught],
    dexNumber = this[DexPokemonTable.dexNumber],
)
