package com.github.aleperaltabazas.dex.service

import com.github.aleperaltabazas.dex.dto.dex.LoginRequestDTO
import com.github.aleperaltabazas.dex.model.UserDex

class LoginService(
    private val usersService: UsersService,
) {
    fun loginFromToken(dexToken: String) = usersService.unsafeFindUserByToken(dexToken)

    fun loginFromMail(request: LoginRequestDTO) = usersService
        .findUserByMail(request.mail)
        ?: merge(request)

    private fun merge(request: LoginRequestDTO) = usersService
        .createUser(mail = request.mail, pokedex = request.localDex.map { UserDex(it) })
}