package com.github.aleperaltabazas.dex.db.schema

import org.jetbrains.exposed.dao.id.LongIdTable

object UsersTable : LongIdTable("users") {
    val username = varchar("username", length = 50).uniqueIndex()
}

object PokedexTable : LongIdTable("pokedex") {
    val game = varchar("game", length = 10)
    val type = varchar("type", length = 30)
    val region = varchar("region", length = 20)
    val userId = long("user_id").references(UsersTable.id)
}

object DexPokemonTable : LongIdTable("dex_pokemon") {
    val name = varchar("name", length = 30)
    val dexNumber = integer("pokedex_number")
    val caught = bool("caught")
    val pokedexId = long("pokedex_id").references(PokedexTable.id)
}
