package com.github.aleperaltabazas.dex.service

import arrow.core.Either
import com.fasterxml.jackson.core.type.TypeReference
import com.github.aleperaltabazas.dex.datasource.firebase.FirebaseMessageClient
import com.github.aleperaltabazas.dex.model.Subscription
import com.github.aleperaltabazas.dex.model.User
import com.github.aleperaltabazas.dex.model.UserDex
import com.github.aleperaltabazas.dex.storage.Collection
import com.github.aleperaltabazas.dex.storage.Storage
import com.mongodb.client.model.Filters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.bson.Document
import org.slf4j.LoggerFactory
import javax.xml.bind.JAXBElement

open class NotificationService(
    private val firebase: FirebaseMessageClient,
    private val storage: Storage,
) {
    open fun notifyPokedexChange(
        user: User,
        userDex: UserDex,
    ) {
        val subscribers = storage.query(Collection.SUBSCRIPTIONS)
            .where(Filters.ne("token", null))
            .where(Filters.eq("user_id", user.userId))
            .where(Filters.eq("dex_id", userDex.userDexId))
            .findAll(SUBSCRIPTION_REF)

        GlobalScope.launch(Dispatchers.IO) {
            subscribers.forEach {
                val response = firebase.notify(
                    to = it.token!!,
                    data = mapOf(
                        "userId" to user.userId,
                        "username" to (user.username ?: user.mail),
                        "dexId" to userDex.userDexId,
                        "game" to (userDex.name ?: userDex.game.displayName),
                    ),
                )

                when (response) {
                    is Either.Right -> {
                        LOGGER.info("Notification created, ID: ${response.b.messageId}")
                    }
                    is Either.Left -> LOGGER.error("Error creating notification", response.a)
                }
            }
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(NotificationService::class.java)
        private val SUBSCRIPTION_REF = object : TypeReference<Subscription>() {}
    }
}