package com.github.aleperaltabazas.dex.storage

import com.github.aleperaltabazas.dex.db.extensions.findUser
import com.github.aleperaltabazas.dex.db.schema.SessionsTable
import com.github.aleperaltabazas.dex.db.schema.UsersTable
import com.github.aleperaltabazas.dex.model.User
import com.github.aleperaltabazas.dex.utils.HashHelper
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction

class UsersStorage(
    private val db: Database,
    private val hash: HashHelper,
) {
    fun findByToken(token: String): User = transaction(db) {
        SessionsTable.findUser(token)
    }

    fun createUser() = transaction(db) {
        val userId = UsersTable
            .insertAndGetId { row ->
                row[this.username] = null
            }

        SessionsTable.insert { row ->
            row[this.userId] = userId.value
            row[this.token] = hash.sha256(userId.toString())
        } get SessionsTable.token
    }
}