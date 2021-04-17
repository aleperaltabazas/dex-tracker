package com.github.aleperaltabazas.dex.service

import com.fasterxml.jackson.core.type.TypeReference
import com.github.aleperaltabazas.dex.model.Session
import com.github.aleperaltabazas.dex.model.User
import com.github.aleperaltabazas.dex.model.UserDex
import com.github.aleperaltabazas.dex.storage.Collection
import com.github.aleperaltabazas.dex.storage.Storage
import com.github.aleperaltabazas.dex.utils.IdGenerator
import org.bson.Document

open class UsersService(
    private val storage: Storage,
    private val idGenerator: IdGenerator,
) {
    open fun updateUser(user: User) {
        storage.replace(Collection.USERS)
            .where(Document("user_id", user.userId))
            .set(user)
            .replaceOne()
    }

    open fun createUserDex(token: String, userDex: UserDex): User? = findUserByToken(token)
        ?.addDex(userDex)
        ?.also { updateUser(it) }

    open fun findUserByToken(token: String) = storage.query(Collection.SESSIONS)
        .where(Document("token", token))
        .findOne(SESSION_REF)
        ?.let {
            storage.query(Collection.USERS)
                .where(Document("user_id", it.userId))
                .findOne(USER_REF)
        }

    open fun findUserById(userId: String): User? = storage.query(Collection.USERS)
        .where(Document("user_id", userId))
        .findOne(USER_REF)

    open fun findUserByMail(mail: String) = storage.query(Collection.USERS)
        .where(Document("mail", mail))
        .findOne(USER_REF)

    open fun createUser(mail: String, pokedex: List<UserDex> = emptyList()): User = User(
        userId = idGenerator.userId(),
        username = null,
        pokedex = pokedex,
        mail = mail
    ).also {
        storage.insert(Collection.USERS, it)
    }

    companion object {
        private val SESSION_REF = object : TypeReference<Session>() {}
        private val USER_REF = object : TypeReference<User>() {}
    }
}