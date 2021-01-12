package com.github.aleperaltabazas.dex.db.dao

import com.github.aleperaltabazas.dex.db.schema.DexPokemonTable
import com.github.aleperaltabazas.dex.db.schema.PokedexTable
import com.github.aleperaltabazas.dex.db.schema.UsersTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserDAO>(UsersTable)

    val username by UsersTable.username
    val pokedex by PokedexDAO referrersOn PokedexTable.userId
}

class PokedexDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<PokedexDAO>(PokedexTable)

    val game by PokedexTable.game
    val type by PokedexTable.type
    val region by PokedexTable.region
    val pokemon by DexPokemonDAO referrersOn DexPokemonTable.pokedexId
}

class DexPokemonDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<DexPokemonDAO>(DexPokemonTable)

    val name by DexPokemonTable.name
    val dexNumber by DexPokemonTable.dexNumber
    val caught by DexPokemonTable.caught
}
