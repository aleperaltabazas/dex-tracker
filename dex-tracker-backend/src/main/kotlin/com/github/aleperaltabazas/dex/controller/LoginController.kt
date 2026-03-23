package com.github.aleperaltabazas.dex.controller

import com.github.aleperaltabazas.dex.constants.DEX_TOKEN
import com.github.aleperaltabazas.dex.dto.dex.LoginRequestDTO
import com.github.aleperaltabazas.dex.exception.BadRequestException
import com.github.aleperaltabazas.dex.extension.dexToken
import com.github.aleperaltabazas.dex.model.User
import com.github.aleperaltabazas.dex.service.LoginService
import com.github.aleperaltabazas.dex.service.SessionService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController(
    private val loginService: LoginService,
    private val sessionService: SessionService,
) {
    @PostMapping("/api/v1/login")
    fun login(
        @RequestBody(required = false) loginRequest: LoginRequestDTO?,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): User {
        val dexToken = request.dexToken()

        return if (dexToken != null) {
            loginService.loginFromToken(dexToken)
        } else {
            val body = loginRequest ?: throw BadRequestException("Missing login request body")
            loginService.loginFromMail(body).also {
                val token = sessionService.createSession(key = body.googleToken, userId = it.userId)
                val cookie = Cookie(DEX_TOKEN, token).apply {
                    path = "/"
                    maxAge = 36000000
                    isHttpOnly = false
                }
                response.addCookie(cookie)
                response.setHeader(DEX_TOKEN, token)
            }
        }
    }

    @PostMapping("/api/v1/logout")
    fun logout(request: HttpServletRequest, response: HttpServletResponse) {
        request.dexToken()?.let { loginService.logout(it) }
        val cookie = Cookie(DEX_TOKEN, "").apply {
            path = "/"
            maxAge = 0
        }
        response.addCookie(cookie)
    }

    @PostMapping("/api/v1/token")
    fun validate(request: HttpServletRequest) {
        val dexToken = request.getHeader(DEX_TOKEN)
            ?: throw BadRequestException("Missing $DEX_TOKEN to validate")
        loginService.loginFromToken(dexToken)
    }
}
