package com.github.aleperaltabazas.dex.db.extensions

import com.github.aleperaltabazas.dex.db.Where
import com.github.aleperaltabazas.dex.db.schema.DexPokemonTable
import com.github.aleperaltabazas.dex.db.schema.PokedexTable
import com.github.aleperaltabazas.dex.db.schema.UsersTable
import com.github.aleperaltabazas.dex.model.PokedexType
import com.github.aleperaltabazas.dex.model.User
import com.github.aleperaltabazas.dex.model.UserDex
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

fun UsersTable.insert(user: User) = insert { row ->
    row[username] = user.username
}
    .also {
        PokedexTable.insert(it[id].value, user.pokedex)
    }

fun UsersTable.selectWhere(where: Where) = this
    .leftJoin(PokedexTable)
    .leftJoin(DexPokemonTable)
    .select(where)
    .groupBy { row -> User(username = row[username], pokedex = emptyList()) }
    .mapValues {
        it.value.groupBy { row ->
            UserDex(
                game = row[PokedexTable.game],
                type = PokedexType.valueOf(row[PokedexTable.type]),
                region = row[PokedexTable.region],
                pokemon = emptyList(),
            )
        }
            .map { (dex, rows) ->
                dex.copy(
                    pokemon = rows.map { it.toUserDexPokemon() }
                )
            }
    }
    .map { (user, pokedex) -> user.copy(pokedex = pokedex) }
