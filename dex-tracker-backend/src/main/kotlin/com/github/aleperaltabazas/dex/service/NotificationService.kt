package com.github.aleperaltabazas.dex.service

import arrow.core.Either
import arrow.core.Ior
import com.github.aleperaltabazas.dex.datasource.firebase.FirebaseMessageClient
import com.github.aleperaltabazas.dex.model.User
import com.github.aleperaltabazas.dex.model.UserDex
import com.github.aleperaltabazas.dex.storage.Collection
import com.github.aleperaltabazas.dex.storage.Storage
import org.slf4j.LoggerFactory

open class NotificationService(
    private val firebase: FirebaseMessageClient,
    private val storage: Storage,
) {
    open fun notifyPokedexChange(
        user: User,
        dexId: String,
    ) {
        val response = firebase.notify(
            to = "/topics/${user.userId}---$dexId",
            data = mapOf(
                "userId" to user.userId,
                "dexId" to dexId,
            ),
        )

        when (response){
            is Either.Right -> {
                LOGGER.info("Notification created, ID: ${response.b.messageId}")
//                storage.insert(Collection.MESSAGES, response.b.messageId)
            }
            is Either.Left -> LOGGER.error("Error creating notification", response.a)
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(NotificationService::class.java)
    }
}