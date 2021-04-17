package com.github.aleperaltabazas.dex.service

import com.github.aleperaltabazas.dex.dto.dex.LoginRequestDTO
import com.github.aleperaltabazas.dex.exception.UnauthorizedException

class LoginService(
    private val usersService: UsersService,
    private val sessionService: SessionService,
) {
    fun loginFromToken(dexToken: String) = usersService.findUserByToken(dexToken)
        ?: throw UnauthorizedException("No session found for token $dexToken")

    fun loginFromMail(request: LoginRequestDTO) = usersService
        .findUserByMail(request.mail)
        ?: create(request)

    fun logout(dexToken: String) = sessionService.deleteSession(dexToken)

    private fun create(request: LoginRequestDTO) = usersService
        .createUser(mail = request.mail)
}