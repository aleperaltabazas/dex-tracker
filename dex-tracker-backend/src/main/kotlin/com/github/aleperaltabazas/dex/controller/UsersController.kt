package com.github.aleperaltabazas.dex.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.constants.APPLICATION_JSON
import com.github.aleperaltabazas.dex.constants.DEX_TOKEN
import com.github.aleperaltabazas.dex.exception.BadRequestException
import com.github.aleperaltabazas.dex.model.User
import com.github.aleperaltabazas.dex.service.UsersService
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
        }
    }

    private fun findUser(req: Request, res: Response): User {
        val dexToken = requireNotNull(req.cookie(DEX_TOKEN)) {
            throw BadRequestException("User has no dex-token stored")
        }

        return usersService.findUser(dexToken)
    }

    private fun createUser(req: Request, res: Response) {
        require(req.cookie(DEX_TOKEN) == null) {
            throw BadRequestException("User already has a token stored")
        }

        val dexToken = usersService.createUser()
        res.cookie(DEX_TOKEN, dexToken)
    }
}