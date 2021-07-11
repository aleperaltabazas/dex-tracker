package com.github.aleperaltabazas.dex.dto

data class MessageRequestDTO<T>(
    val to: String,
    val notification: NotificationDTO?,
    val data: T?,
)

data class NotificationDTO(
    val title: String,
    val body: String,
)

data class MessageDTO(
    val messageId: String,
)
