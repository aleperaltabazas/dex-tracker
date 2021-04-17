package com.github.aleperaltabazas.dex.service

import com.fasterxml.jackson.core.type.TypeReference
import com.github.aleperaltabazas.dex.model.Session
import com.github.aleperaltabazas.dex.storage.Collection
import com.github.aleperaltabazas.dex.storage.Storage
import com.github.aleperaltabazas.dex.utils.HashHelper
import org.bson.Document

open class SessionService(
    private val storage: Storage,
    private val hash: HashHelper
) {
    open fun findSession(key: String): Session? = storage.query(Collection.SESSIONS)
        .where(Document("token", key))
        .findOne(SESSION_REF)

    open fun createSession(key: String, userId: String) = hash.sha256(key).also { token ->
        storage.insert(Collection.SESSIONS, Session(token = token, userId = userId))
    }

    open fun deleteSession(token: String) = storage
        .delete(Collection.SESSIONS)
        .where(Document("token", token))
        .deleteOne()

    companion object {
        private val SESSION_REF = object : TypeReference<Session>() {}
    }
}