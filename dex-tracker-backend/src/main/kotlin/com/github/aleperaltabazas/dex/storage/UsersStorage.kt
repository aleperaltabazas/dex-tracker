package com.github.aleperaltabazas.dex.storage

import com.github.aleperaltabazas.dex.db.Where
import com.github.aleperaltabazas.dex.db.extensions.findUser
import com.github.aleperaltabazas.dex.db.extensions.insert
import com.github.aleperaltabazas.dex.db.extensions.selectWhere
import com.github.aleperaltabazas.dex.db.extensions.updateCaught
import com.github.aleperaltabazas.dex.db.schema.DexPokemonTable
import com.github.aleperaltabazas.dex.db.schema.PokedexTable
import com.github.aleperaltabazas.dex.db.schema.SessionsTable
import com.github.aleperaltabazas.dex.db.schema.UsersTable
import com.github.aleperaltabazas.dex.model.User
import com.github.aleperaltabazas.dex.model.UserDex
import com.github.aleperaltabazas.dex.utils.HashHelper
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction

class UsersStorage(
    private val db: Database,
    private val hash: HashHelper,
) {
    fun createUserDex(
        userId: Long,
        userDex: UserDex
    ): UserDex = transaction(db) {
        val id = PokedexTable.insert(
            userId = userId,
            pokedex = userDex
        ) get PokedexTable.id

        userDex.copy(id = id.value)
    }

    fun updateCaughtStatus(
        pokedexId: Long,
        dexNumber: Int,
        caught: Boolean
    ) = transaction(db) {
        DexPokemonTable.updateCaught(
            caught = caught,
            pokedexId = pokedexId,
            dexNumber = dexNumber
        )
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

    fun exists(where: Where) = transaction(db) {
        UsersTable.selectWhere(where).count() > 0
    }
}