package com.github.aleperaltabazas.dex.controller

import com.github.aleperaltabazas.dex.constants.DEX_TOKEN
import com.github.aleperaltabazas.dex.dto.dex.LoginRequestDTO
import com.github.aleperaltabazas.dex.exception.BadRequestException
import com.github.aleperaltabazas.dex.model.User
import com.github.aleperaltabazas.dex.service.LoginService
import com.github.aleperaltabazas.dex.service.SessionService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class LoginController(
    private val loginService: LoginService,
    private val sessionService: SessionService,
) {
    @PostMapping("/login")
    fun login(
        @RequestBody loginRequest: LoginRequestDTO,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): User {
        val dexToken = request.dexToken()

        return if (dexToken != null) {
            loginService.loginFromToken(dexToken)
        } else {
            loginService.loginFromMail(loginRequest).also { user ->
                val token = sessionService.createSession(key = loginRequest.googleToken, userId = user.userId)
                response.addCookie(Cookie(DEX_TOKEN, token).apply {
                    path = "/"
                    maxAge = 36000000
                    isHttpOnly = false
                })
                response.setHeader(DEX_TOKEN, token)
            }
        }
    }

    @PostMapping("/token")
    fun validate(request: HttpServletRequest): User {
        val dexToken = request.getHeader(DEX_TOKEN) ?: throw BadRequestException("Missing $DEX_TOKEN to validate")
        return loginService.loginFromToken(dexToken)
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(request: HttpServletRequest, response: HttpServletResponse) {
        request.dexToken()?.let { loginService.logout(it) }
        response.addCookie(Cookie(DEX_TOKEN, "").apply {
            path = "/"
            maxAge = 0
        })
    }

    private fun HttpServletRequest.dexToken(): String? =
        getHeader(DEX_TOKEN) ?: cookies?.find { it.name == DEX_TOKEN }?.value
}
