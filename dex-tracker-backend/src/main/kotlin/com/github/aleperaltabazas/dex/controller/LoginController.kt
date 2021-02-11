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
import spark.Spark

class LoginController(
    private val objectMapper: ObjectMapper,
    private val loginService: LoginService,
    private val modelMapper: ModelMapper,
    private val sessionService: SessionService,
) : Controller {
    override fun register() {
        Spark.post("/login", APPLICATION_JSON, this::login, objectMapper::writeValueAsString)
    }

    private fun login(req: Request, res: Response): UserDTO {
        val loginInfo: LoginRequestDTO = objectMapper.readValue(req.body())
        val dexToken: String? = req.cookie(DEX_TOKEN)

        val user = loginService.login(loginInfo, dexToken)
        val token = sessionService.createSession(user.userId)

        res.cookie("/", DEX_TOKEN, token, 36000000, false)

        return modelMapper.mapUserToDTO(user)
    }
}