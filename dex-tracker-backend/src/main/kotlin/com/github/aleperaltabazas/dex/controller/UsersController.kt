package com.github.aleperaltabazas.dex.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.constants.APPLICATION_JSON
import com.github.aleperaltabazas.dex.constants.DEX_TOKEN
import com.github.aleperaltabazas.dex.dto.dex.UserDTO
import com.github.aleperaltabazas.dex.exception.BadRequestException
import com.github.aleperaltabazas.dex.service.UsersService
import org.slf4j.LoggerFactory
import spark.Request
import spark.Response
import spark.Spark.*

class UsersController(
    private val objectMapper: ObjectMapper,
    private val usersService: UsersService,
) : Controller {
    override fun register() {
        path("/api/v1/users") {
            get("", APPLICATION_JSON, this::findUser, objectMapper::writeValueAsString)
            post("", APPLICATION_JSON, this::createUser, objectMapper::writeValueAsString)
            before("") { req, res ->
                val headers = req.headers()
                    .joinToString(",") { "\"$it:${req.headers(it)}}\"" }

                LOGGER.info("[${req.requestMethod()}]  Request headers: $headers")
            }
            after("") { _, res ->
                LOGGER.info("Response: ${res.status()}")
            }
        }
    }

    private fun findUser(req: Request, res: Response): UserDTO {
        val dexToken = requireNotNull(req.cookie(DEX_TOKEN)) {
            throw BadRequestException("User has no dex-token stored")
        }

        return UserDTO(usersService.findUser(dexToken))
    }

    private fun createUser(req: Request, res: Response): UserDTO {
        require(req.cookie(DEX_TOKEN) == null) {
            throw BadRequestException("User already has a token stored")
        }

        val (user, dexToken) = usersService.createUser()
        res.cookie("/", DEX_TOKEN, dexToken, 36000000, false)

        return UserDTO(user)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(UsersController::class.java)
    }
}