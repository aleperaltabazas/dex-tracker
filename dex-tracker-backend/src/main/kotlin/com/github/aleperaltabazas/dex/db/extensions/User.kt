package com.github.aleperaltabazas.dex.db.extensions

import com.github.aleperaltabazas.dex.db.Where
import com.github.aleperaltabazas.dex.db.schema.DexPokemonTable
import com.github.aleperaltabazas.dex.db.schema.PokedexTable
import com.github.aleperaltabazas.dex.db.schema.UsersTable
import com.github.aleperaltabazas.dex.model.PokedexType
import com.github.aleperaltabazas.dex.model.User
import com.github.aleperaltabazas.dex.model.UserDex
import com.github.aleperaltabazas.dex.model.UserDexPokemon
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

fun UsersTable.insert(vararg users: User) = users.map { user ->
    insert { row ->
        row[username] = user.username
    }
        .also {
            PokedexTable.insert(it[id].value, user.pokedex)
        }
}

fun UsersTable.selectWhere(where: Where) = this
    .leftJoin(PokedexTable)
    .leftJoin(DexPokemonTable)
    .select(where)

private fun ResultRow.hasUserDexValue() = getOrNull(PokedexTable.game) != null &&
    getOrNull(PokedexTable.region) != null &&
    getOrNull(PokedexTable.type) != null

private fun ResultRow.hasUserDexPokemonValue() = getOrNull(DexPokemonTable.name) != null &&
    getOrNull(DexPokemonTable.dexNumber) != null &&
    getOrNull(DexPokemonTable.caught) != null

fun Query.toUsers(): List<User> = this
    .groupBy { row ->
        User(
            id = row[UsersTable.id].value,
            username = row[UsersTable.username],
            pokedex = emptyList()
        )
    }
    .map { (user, rows) ->
        val pokedex = rows
            .filter { row -> row.hasUserDexValue() }
            .groupBy { row ->
                UserDex(
                    game = row[PokedexTable.game],
                    region = row[PokedexTable.region],
                    type = row[PokedexTable.type].let { PokedexType.valueOf(it) },
                    pokemon = emptyList()
                )
            }
            .mapValues { (_, rows) ->
                rows
                    .filter { it.hasUserDexPokemonValue() }
                    .map {
                        UserDexPokemon(
                            name = it[DexPokemonTable.name],
                            dexNumber = it[DexPokemonTable.dexNumber],
                            caught = it[DexPokemonTable.caught]
                        )
                    }
            }
            .map { (userDex, pokemon) -> userDex.copy(pokemon = pokemon) }

        user.copy(pokedex = pokedex)
    }
