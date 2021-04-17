package com.github.aleperaltabazas.dex.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.constants.APPLICATION_JSON
import com.github.aleperaltabazas.dex.constants.DEX_TOKEN
import com.github.aleperaltabazas.dex.exception.NotFoundException
import com.github.aleperaltabazas.dex.extension.paramNotNull
import com.github.aleperaltabazas.dex.model.User
import com.github.aleperaltabazas.dex.service.PokedexService
import com.github.aleperaltabazas.dex.service.SessionService
import com.github.aleperaltabazas.dex.service.UsersService
import spark.Request
import spark.Response
import spark.Spark.*

class UsersController(
    private val objectMapper: ObjectMapper,
    private val usersService: UsersService,
    private val pokedexService: PokedexService,
    private val sessionService: SessionService,
) : Controller {
    override fun register() {
        path("/api/v1/users") {
            get("/:id", APPLICATION_JSON, this::findUser, objectMapper::writeValueAsString)
//            get("", APPLICATION_JSON, this::findUser, objectMapper::writeValueAsString)
//            get("/pokedex", APPLICATION_JSON, this::usersPokedex, objectMapper::writeValueAsString)
//            post("/pokedex", APPLICATION_JSON, this::createUserDex, objectMapper::writeValueAsString)
//            get("/pokedex/:id", APPLICATION_JSON, this::findUserDex, objectMapper::writeValueAsString)
//            get("/:username", APPLICATION_JSON, this::findUsername, objectMapper::writeValueAsString)
//            get("/:username/pokedex/:name", APPLICATION_JSON, this::findUserPublicDex, objectMapper::writeValueAsString)
//            patch("", APPLICATION_JSON, this::updateUser, objectMapper::writeValueAsString)
//            patch("/pokedex", APPLICATION_JSON, this::updateUserDexCaughtStatus, objectMapper::writeValueAsString)
        }
    }

    private fun findUser(req: Request, res: Response): User {
        val userId = req.paramNotNull(":id")
        val user = usersService.findUserById(userId) ?: throw NotFoundException("No user found for user id $userId")
        val session = req.cookie(DEX_TOKEN)?.let(sessionService::findSession)

        return user.takeUnless { it.userId == session?.userId }
            ?.filterPublic()
            ?: user
    }
}