package com.github.aleperaltabazas.dex.storage

import com.github.aleperaltabazas.dex.db.extensions.findUser
import com.github.aleperaltabazas.dex.db.schema.SessionsTable
import com.github.aleperaltabazas.dex.model.User
import org.jetbrains.exposed.sql.Database

class UsersStorage(
    private val db: Database
) {
    fun findByToken(token: String): User = SessionsTable.findUser(token)
}