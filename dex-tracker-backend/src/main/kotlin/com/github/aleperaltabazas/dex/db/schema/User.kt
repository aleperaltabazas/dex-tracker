package com.github.aleperaltabazas.dex.db.schema

import com.github.aleperaltabazas.dex.db.GenericTable
import com.github.aleperaltabazas.dex.db.model.DexPokemon
import com.github.aleperaltabazas.dex.db.model.Pokedex
import com.github.aleperaltabazas.dex.db.model.User
import com.github.aleperaltabazas.dex.db.reifying
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select

object Users : GenericTable<User>("users") {
    val username = varchar("username", length = 50)

    override fun reify(row: ResultRow): User = User(
        id = row[id],
        username = row[username],
        pokedexes = Pokedexes.reifying { select { Pokedexes.userId eq row[id] } }.toList()
    )
}

object Pokedexes : GenericTable<Pokedex>("pokedexes") {
    val game = varchar("game", length = 10)
    val type = varchar("type", length = 30)
    val region = varchar("region", length = 20)
    val userId = long("user_id") references Users.id

    override fun reify(row: ResultRow): Pokedex = Pokedex(
        id = row[id],
        game = row[game],
        type = row[type],
        region = row[region],
        pokemons = DexPokemons.reifying { select { DexPokemons.pokedexId eq row[id] } }.toList()
    )
}

object DexPokemons : GenericTable<DexPokemon>("dex_pokemons") {
    val name = varchar("name", length = 30)
    val dexNumber = integer("pokedex_number")
    val caught = bool("caught")
    val pokedexId = long("pokedex_id") references Pokedexes.id

    override fun reify(row: ResultRow): DexPokemon = DexPokemon(
        id = row[id],
        name = row[name],
        dexNumber = row[dexNumber],
        caught = row[caught],
    )
}
