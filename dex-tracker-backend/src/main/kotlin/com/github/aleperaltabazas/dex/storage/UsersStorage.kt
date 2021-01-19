package com.github.aleperaltabazas.dex.storage

import com.github.aleperaltabazas.dex.db.extensions.findUser
import com.github.aleperaltabazas.dex.db.extensions.userRows
import com.github.aleperaltabazas.dex.db.schema.DexPokemonTable
import com.github.aleperaltabazas.dex.db.schema.PokedexTable
import com.github.aleperaltabazas.dex.db.schema.SessionsTable
import com.github.aleperaltabazas.dex.db.schema.UsersTable
import com.github.aleperaltabazas.dex.dto.dex.CaughtStatusDTO
import com.github.aleperaltabazas.dex.model.User
import com.github.aleperaltabazas.dex.utils.HashHelper
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class UsersStorage(
    private val db: Database,
    private val hash: HashHelper,
) {
    fun updateUserCaughtStatus(
        token: String,
        status: List<CaughtStatusDTO>,
    ) = transaction(db) {
        status.forEach {
            SessionsTable.userRows(token)
                .update({ (PokedexTable.id eq it.pokedexId) and (DexPokemonTable.dexNumber eq it.dexNumber) }) { row ->
                    row[DexPokemonTable.caught] = it.caught
                }
        }
    }

    fun findByToken(token: String): User = transaction(db) {
        SessionsTable.findUser(token)
    }

    fun createUser() = transaction(db) {
        val userId = UsersTable
            .insertAndGetId { row ->
                row[this.username] = null
            }

        val token = SessionsTable.insert { row ->
            row[this.userId] = userId.value
            row[this.token] = hash.sha256(userId.toString())
        } get SessionsTable.token

        User(username = null, pokedex = emptyList()) to token
    }
}