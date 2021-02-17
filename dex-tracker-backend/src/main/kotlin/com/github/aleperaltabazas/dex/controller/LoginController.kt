package com.github.aleperaltabazas.dex.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.aleperaltabazas.dex.constants.APPLICATION_JSON
import com.github.aleperaltabazas.dex.constants.DEX_TOKEN
import com.github.aleperaltabazas.dex.dto.dex.LoginRequestDTO
import com.github.aleperaltabazas.dex.dto.dex.UserDTO
import com.github.aleperaltabazas.dex.mapper.ModelMapper
import com.github.aleperaltabazas.dex.service.LoginService
import com.github.aleperaltabazas.dex.service.SessionService
import spark.Request
import spark.Response
import spark.Spark.post

class LoginController(
    private val objectMapper: ObjectMapper,
    private val loginService: LoginService,
    private val modelMapper: ModelMapper,
    private val sessionService: SessionService,
) : Controller {
    override fun register() {
        post("/api/v1/login", APPLICATION_JSON, this::login, objectMapper::writeValueAsString)
        post("/api/v1/logout", APPLICATION_JSON, this::logout, objectMapper::writeValueAsString)
    }

    private fun login(req: Request, res: Response): UserDTO {
        val dexToken = req.cookie(DEX_TOKEN)

        val user = if (dexToken != null) {
            loginService.loginFromToken(dexToken)
        } else {
            val loginRequest = objectMapper.readValue<LoginRequestDTO>(req.body())
            loginService.loginFromMail(loginRequest).also {
                val token = sessionService.createSession(key = loginRequest.googleToken, userId = it.userId)
                res.cookie("/", DEX_TOKEN, token, 36000000, false)
            }
        }

        return modelMapper.mapUserToDTO(user)
    }

    private fun logout(req: Request, res: Response) {
        req.cookie(DEX_TOKEN)?.let { loginService.logout(it) }
        res.cookie("/", DEX_TOKEN, "", 0, false)
    }
}