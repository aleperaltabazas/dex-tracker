package com.github.aleperaltabazas.dex.service

import com.fasterxml.jackson.core.type.TypeReference
import com.github.aleperaltabazas.dex.dto.dex.CaughtStatusDTO
import com.github.aleperaltabazas.dex.exception.ForbiddenException
import com.github.aleperaltabazas.dex.exception.NotFoundException
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

    open fun createUserDex(token: String, userDex: UserDex) {
        val user = unsafeFindUserByToken(token)

        storage.update(Collection.USERS)
            .where(Document("user_id", user.userId))
            .add("pokedex", userDex)
            .updateOne()
    }

    open fun unsafeFindUserByToken(token: String) = findUserByToken(token)
        ?: throw NotFoundException("No user found for session token $token")

    open fun findUserByToken(token: String) = storage.query(Collection.SESSIONS)
        .where(Document("token", token))
        .findOne(SESSION_REF)
        ?.let {
            storage.query(Collection.USERS)
                .where(Document("user_id", it.userId))
                .findOne(USER_REF)
        }

    open fun findUserById(userId: String) = storage.query(Collection.USERS)
        .where(Document("user_id", userId))
        .findOne(USER_REF)

    open fun findUserByMail(mail: String) = storage.query(Collection.USERS)
        .where(Document("mail", mail))
        .findOne(USER_REF)

    open fun createUser(mail: String, pokedex: List<UserDex> = emptyList()): User {
        val userId = idGenerator.userId()

        val user = User(
            userId = userId,
            username = null,
            pokedex = pokedex,
            mail = mail
        )

        storage.insert(Collection.USERS, user)

        return user
    }

    open fun findUserDex(token: String, dexId: String): UserDex = unsafeFindUserByToken(token).let { user ->
        user.pokedex
            .find { it.userDexId == dexId }
            ?: throw NotFoundException("User dex with id $dexId not found for user ${user.userId}")
    }

    open fun updateCaughtStatus(token: String, status: List<CaughtStatusDTO>) {
        val user = this.unsafeFindUserByToken(token)

        for (s in status) {
            if (!user.owns(s.pokedexId)) {
                throw ForbiddenException("User is not allowed to edit pokedex by ${s.pokedexId}")
            }
        }

        val dexToUpdate = status.groupBy { it.pokedexId }

        storage.replace(collection = Collection.USERS)
            .where(Document("user_id", user.userId))
            .set(
                value = user.copy(
                    pokedex = dexToUpdate.toList().fold(user) { u, (dexId, status) ->
                        u.updatePokedex(
                            dexId,
                            status
                        )
                    }.pokedex
                )
            )
            .replaceOne()
    }

    companion object {
        private val SESSION_REF = object : TypeReference<Session>() {}
        private val USER_REF = object : TypeReference<User>() {}
    }
}