package com.github.aleperaltabazas.dex.service

import arrow.core.extensions.list.foldable.nonEmpty
import com.fasterxml.jackson.core.type.TypeReference
import com.github.aleperaltabazas.dex.dto.dex.UpdateUserDTO
import com.github.aleperaltabazas.dex.exception.ForbiddenException
import com.github.aleperaltabazas.dex.model.User
import com.github.aleperaltabazas.dex.model.UserDex
import com.github.aleperaltabazas.dex.storage.Collection
import com.github.aleperaltabazas.dex.storage.Storage
import com.github.aleperaltabazas.dex.utils.IdGenerator
import org.bson.Document

open class UsersService(
    private val storage: Storage,
    private val sessionService: SessionService,
    private val idGenerator: IdGenerator,
    private val notificationService: NotificationService,
) {
    open fun updateUser(userId: String, changes: UpdateUserDTO): User? = findUserById(userId)
        ?.let { user ->
            changes.dex?.keys
                ?.filterNot { user.owns(it) }
                ?.takeIf { it.nonEmpty() }
                ?.let { throw ForbiddenException("User $userId is not allowed to edit dex ${it.joinToString(",")}") }

            user.update(changes)
                .also {
                    updateUser(it)
                    changes.dex?.keys?.forEach { id ->
                        notificationService.notifyPokedexChange(
                            user = it,
                            userDex = user.pokedex.find { d -> d.userDexId == id }!!,
                        )
                    }
                }
        }

    open fun updateUser(user: User) {
        storage.replace(Collection.USERS)
            .where(Document("user_id", user.userId))
            .set(user)
            .replaceOne()
    }

    open fun createUserDex(userId: String, userDex: UserDex): User? = findUserById(userId)
        ?.addDex(userDex)
        ?.also { updateUser(it) }

    open fun findUserByToken(token: String) = sessionService.findSession(token)
        ?.let {
            storage.query(Collection.USERS)
                .where(Document("user_id", it.userId))
                .findOne(USER_REF)
        }

    open fun findUserById(userId: String): User? = storage.query(Collection.USERS)
        .where(Document("user_id", userId))
        .join(
            collection = Collection.SUBSCRIPTIONS,
            localField = "user_id",
            foreignField = "subscriber_user_id",
            `as` = "subscriptions",
        )
        .findOneAggregated(USER_REF)

    open fun findUserByMail(mail: String) = storage.query(Collection.USERS)
        .where(Document("mail", mail))
        .findOne(USER_REF)

    open fun createUser(mail: String, pokedex: List<UserDex> = emptyList()): User = User(
        userId = idGenerator.userId(),
        username = null,
        pokedex = pokedex,
        mail = mail,
    ).also {
        storage.insert(Collection.USERS, it)
    }

    companion object {
        private val USER_REF = object : TypeReference<User>() {}
    }
}