package com.github.aleperaltabazas.dex.db.dao

import com.github.aleperaltabazas.dex.db.schema.DexPokemonTable
import com.github.aleperaltabazas.dex.db.schema.PokedexTable
import com.github.aleperaltabazas.dex.db.schema.UsersTable
import com.github.aleperaltabazas.dex.model.PokedexType
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UsersDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UsersDAO>(UsersTable)

    var username by UsersTable.username
    val pokedex by PokedexDAO referrersOn PokedexTable.userId
}

class PokedexDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<PokedexDAO>(PokedexTable)

    var game by PokedexTable.game
    var type by PokedexTable.type.transform(
        toColumn = { it.name },
        toReal = { PokedexType.valueOf(it) }
    )
    var region by PokedexTable.region
    var userId by PokedexTable.userId
    val pokemon by DexPokemonDAO referrersOn DexPokemonTable.pokedexId
}

class DexPokemonDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<DexPokemonDAO>(DexPokemonTable)

    var name by DexPokemonTable.name
    var dexNumber by DexPokemonTable.dexNumber
    var caught by DexPokemonTable.caught
    var pokedexId by DexPokemonTable.pokedexId
}
