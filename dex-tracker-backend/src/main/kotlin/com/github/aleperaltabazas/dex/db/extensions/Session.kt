package com.github.aleperaltabazas.dex.db.extensions

import com.github.aleperaltabazas.dex.db.schema.DexPokemonTable
import com.github.aleperaltabazas.dex.db.schema.PokedexTable
import com.github.aleperaltabazas.dex.db.schema.SessionsTable
import com.github.aleperaltabazas.dex.db.schema.UsersTable
import com.github.aleperaltabazas.dex.dto.dex.CaughtStatusDTO
import com.github.aleperaltabazas.dex.exception.AmbiguousReferenceException
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

fun SessionsTable.findUser(token: String) = this
    .leftJoin(UsersTable)
    .leftJoin(PokedexTable)
    .leftJoin(DexPokemonTable)
    .select { SessionsTable.token eq token }
    .toUsers()
    .let {
        if (it.size > 1) {
            throw AmbiguousReferenceException("Query for token $token returned ${it.size} users")
        } else {
            it.first()
        }
    }
