package com.github.aleperaltabazas.dex.service

import com.github.aleperaltabazas.dex.datasource.google.GoogleOAuthValidator
import com.github.aleperaltabazas.dex.dto.dex.LoginRequestDTO
import com.github.aleperaltabazas.dex.exception.UnauthorizedException
import com.github.aleperaltabazas.dex.model.User

class LoginService(
    private val usersService: UsersService,
    private val sessionService: SessionService,
    private val google: GoogleOAuthValidator,
) {
    fun loginFromToken(dexToken: String) = usersService.findUserByToken(dexToken)
        ?: throw UnauthorizedException("No session found for token $dexToken")

    fun loginFromMail(request: LoginRequestDTO): User {
        google.validate(request.googleToken)

        return usersService
            .findUserByMail(request.mail)
            ?: create(request)
    }

    fun logout(dexToken: String) = sessionService.deleteSession(dexToken)

    private fun create(request: LoginRequestDTO): User {
        google.validate(request.googleToken)

        return usersService.createUser(mail = request.mail)
    }
}