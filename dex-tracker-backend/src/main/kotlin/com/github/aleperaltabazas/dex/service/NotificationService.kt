package com.github.aleperaltabazas.dex.service

import arrow.core.Either
import arrow.core.Ior
import com.github.aleperaltabazas.dex.datasource.firebase.FirebaseMessageClient
import com.github.aleperaltabazas.dex.model.User
import com.github.aleperaltabazas.dex.model.UserDex
import org.slf4j.LoggerFactory

class NotificationService(
    private val firebase: FirebaseMessageClient,
) {
    fun notifyPokedexChange(
        user: User,
        userDex: UserDex,
    ) {
        val response = firebase.notify(
            to = "/topics/${user.userId}---${userDex.userDexId}",
            data = mapOf(
                "userId" to user.userId,
                "dexId" to userDex.userDexId,
            ),
        )

        when (response){
            is Either.Right -> LOGGER.info("Notification created, ID: ${response.b.messageId}")
            is Either.Left -> LOGGER.error("Error creating notification", response.a)
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(NotificationService::class.java)
    }
}