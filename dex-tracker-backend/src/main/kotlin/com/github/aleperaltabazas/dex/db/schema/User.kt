package com.github.aleperaltabazas.dex.db.schema

import com.github.aleperaltabazas.dex.db.GenericTable
import com.github.aleperaltabazas.dex.db.model.DexPokemon
import com.github.aleperaltabazas.dex.db.model.Pokedex
import com.github.aleperaltabazas.dex.db.model.User

object Users : GenericTable<User>("users") {
    val username = varchar("username", length = 50).uniqueIndex()
}

object Pokedexes : GenericTable<Pokedex>("pokedexes") {
    val game = varchar("game", length = 10)
    val type = varchar("type", length = 30)
    val region = varchar("region", length = 20)
    val userId = long("user_id") references Users.id
}

object DexPokemons : GenericTable<DexPokemon>("dex_pokemons") {
    val name = varchar("name", length = 30)
    val dexNumber = integer("pokedex_number")
    val caught = bool("caught")
    val pokedexId = long("pokedex_id") references Pokedexes.id
}
