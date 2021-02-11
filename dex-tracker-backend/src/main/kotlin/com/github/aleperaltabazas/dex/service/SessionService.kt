package com.github.aleperaltabazas.dex.service

import com.github.aleperaltabazas.dex.model.Session
import com.github.aleperaltabazas.dex.storage.Collection
import com.github.aleperaltabazas.dex.storage.Storage
import com.github.aleperaltabazas.dex.utils.HashHelper

open class SessionService(
    private val storage: Storage,
    private val hash: HashHelper
) {
    open fun createSession(userId: String) = hash.sha256(userId).also { token ->
        storage.insert(Collection.SESSIONS, Session(token = token, userId = userId))
    }
}