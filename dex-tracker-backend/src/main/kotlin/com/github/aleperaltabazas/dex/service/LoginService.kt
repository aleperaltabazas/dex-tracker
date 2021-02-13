package com.github.aleperaltabazas.dex.service

import com.github.aleperaltabazas.dex.dto.dex.LoginRequestDTO
import com.github.aleperaltabazas.dex.model.User
import com.github.aleperaltabazas.dex.model.UserDex

class LoginService(
    private val usersService: UsersService,
    private val sessionService: SessionService,
) {
    fun loginFromToken(dexToken: String) = usersService.unsafeFindUserByToken(dexToken)

    fun loginFromMail(request: LoginRequestDTO) = usersService
        .findUserByMail(request.mail)
        ?.let { this.merge(request, it) }
        ?: create(request)

    fun logout(dexToken: String) = sessionService.deleteSession(dexToken)

    private fun merge(request: LoginRequestDTO, user: User) = user.mergePokedex(
        pokedex = request.localDex.map { UserDex(it) }
    )
        .also { usersService.updateUser(it) }

    private fun create(request: LoginRequestDTO) = usersService
        .createUser(mail = request.mail, pokedex = request.localDex.map { UserDex(it) })
}