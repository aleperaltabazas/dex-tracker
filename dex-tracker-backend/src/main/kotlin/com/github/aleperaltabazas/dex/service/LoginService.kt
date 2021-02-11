package com.github.aleperaltabazas.dex.service

import com.github.aleperaltabazas.dex.dto.dex.LoginRequestDTO
import com.github.aleperaltabazas.dex.model.User
import com.github.aleperaltabazas.dex.storage.Collection
import com.github.aleperaltabazas.dex.storage.Storage
import org.bson.Document

class LoginService(
    private val storage: Storage,
    private val usersService: UsersService,
) {
    fun login(request: LoginRequestDTO, dexToken: String?): User = usersService
        .findUserByMail(request.mail)
        ?: dexToken?.let { merge(it, request.mail) }
        ?: usersService.createUser(username = null, mail = request.mail)

    private fun merge(dexToken: String, mail: String): User? = usersService.findUserByToken(dexToken)
        ?.also {
            storage.update(Collection.USERS)
                .set("mail", mail)
                .where(Document("user_id", it.userId))
                .updateOne()
        }
        ?.copy(mail = mail)
}