package com.github.aleperaltabazas.dex.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.aleperaltabazas.dex.constants.APPLICATION_JSON
import com.github.aleperaltabazas.dex.constants.DEX_TOKEN
import com.github.aleperaltabazas.dex.dto.dex.CaughtStatusDTO
import com.github.aleperaltabazas.dex.dto.dex.CreateUserDexDTO
import com.github.aleperaltabazas.dex.dto.dex.UserDTO
import com.github.aleperaltabazas.dex.exception.BadRequestException
import com.github.aleperaltabazas.dex.exception.UnauthorizedException
import com.github.aleperaltabazas.dex.extension.prettyHeaders
import com.github.aleperaltabazas.dex.model.UserDex
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
            post("/pokedex", APPLICATION_JSON, this::createUserDex, objectMapper::writeValueAsString)
            patch("/pokedex/:id", APPLICATION_JSON, this::updateUserDexCaughtStatus, objectMapper::writeValueAsString)

            before("") { req, res ->
                LOGGER.info("[${req.requestMethod()}] ${req.contextPath()} Request headers: ${req.prettyHeaders()}")
            }
            after("") { _, res ->
                LOGGER.info("Response: ${res.status()}")
            }
        }
    }

    private fun createUserDex(req: Request, res: Response): UserDex {
        val token = requireNotNull(req.cookie(DEX_TOKEN)) {
            throw UnauthorizedException("No dex-token found for")
        }

        val body: CreateUserDexDTO = objectMapper.readValue(req.body())

        return usersService.createUserDex(
            token = token,
            game = body.game,
            type = body.type
        )
    }

    private fun updateUserDexCaughtStatus(req: Request, res: Response) {
        val dexToken = requireNotNull(req.cookie(DEX_TOKEN)) {
            throw UnauthorizedException("User has no dex-token stored")
        }
        val status: List<CaughtStatusDTO> = objectMapper.readValue(req.body())

        usersService.updateCaughtStatus(
            token = dexToken,
            status = status,
        )

        res.status(200)
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

        val (user, dexToken) = usersService.createUser(null)
        res.cookie("/", DEX_TOKEN, dexToken, 36000000, false)

        return UserDTO(user)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(UsersController::class.java)
    }
}