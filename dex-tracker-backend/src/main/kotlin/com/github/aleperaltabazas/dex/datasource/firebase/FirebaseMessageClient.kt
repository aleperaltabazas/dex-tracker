package com.github.aleperaltabazas.dex.datasource.firebase

import com.fasterxml.jackson.core.type.TypeReference
import com.github.aleperaltabazas.dex.connector.RestConnector
import com.github.aleperaltabazas.dex.dto.MessageDTO
import com.github.aleperaltabazas.dex.dto.MessageRequestDTO
import com.github.aleperaltabazas.dex.dto.NotificationDTO
import com.github.aleperaltabazas.dex.extension.both

class FirebaseMessageClient(
    private val firebaseConnector: RestConnector,
) {
    fun <T> notify(
        to: String,
        title: String,
        body: String,
        data: T,
    ) = doNotify(
        to = to,
        title = title,
        body = body,
        data = data,
    )

    fun notify(
        to: String,
        title: String,
        body: String,
    ) = doNotify(
        to = to,
        title = title,
        body = body,
        data = null,
    )

    fun <T> notify(
        to: String,
        data: T,
    ) = doNotify(
        to = to,
        title = null,
        body = null,
        data = data,
    )

    private fun <T> doNotify(
        to: String,
        title: String?,
        body: String?,
        data: T?,
    ) = firebaseConnector.post(
        path = "/fcm/send",
        body = MessageRequestDTO(
            to = to,
            data = data,
            notification = both(
                { title },
                { body },
            )?.let { (t, b) ->
                NotificationDTO(
                    title = t,
                    body = b,
                )
            }
        )
    )
        .map { it.deserializeAs(MESSAGE_REF) }

    companion object {
        private val MESSAGE_REF = object : TypeReference<MessageDTO>() {}
    }
}