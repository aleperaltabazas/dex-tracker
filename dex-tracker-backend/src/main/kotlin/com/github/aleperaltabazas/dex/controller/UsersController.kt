package com.github.aleperaltabazas.dex.controller

import com.fasterxml.jackson.databind.ObjectMapper
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
            get("", "application/json", this::findUser, objectMapper::writeValueAsString)
            post("", "application/json", this::createUser, objectMapper::writeValueAsString)
        }
    }

    private fun findUser(req: Request, res: Response): User {
        val dexToken = requireNotNull(req.cookie("dex-token")) {
            throw BadRequestException("User has no dex-token stored")
        }

        return usersService.findUser(dexToken)
    }

    private fun createUser(req: Request, res: Response) {
        require(req.cookie("dex-token") == null) {
            throw BadRequestException("User already has a token stored")
        }

        val dexToken = usersService.createUser()
        res.cookie("dex-token", dexToken)
    }
}