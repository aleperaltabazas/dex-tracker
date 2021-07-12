package com.github.aleperaltabazas.dex.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.aleperaltabazas.dex.constants.APPLICATION_JSON
import com.github.aleperaltabazas.dex.dto.dex.CreateDexDTO
import com.github.aleperaltabazas.dex.dto.dex.SubscribeDTO
import com.github.aleperaltabazas.dex.dto.dex.UpdateUserDTO
import com.github.aleperaltabazas.dex.exception.ForbiddenException
import com.github.aleperaltabazas.dex.exception.NotFoundException
import com.github.aleperaltabazas.dex.exception.UnauthorizedException
import com.github.aleperaltabazas.dex.extension.dexToken
import com.github.aleperaltabazas.dex.extension.paramNotNull
import com.github.aleperaltabazas.dex.model.Session
import com.github.aleperaltabazas.dex.model.User
import com.github.aleperaltabazas.dex.model.UserDex
import com.github.aleperaltabazas.dex.service.PokedexService
import com.github.aleperaltabazas.dex.service.SessionService
import com.github.aleperaltabazas.dex.service.SubscriptionService
import com.github.aleperaltabazas.dex.service.UsersService
import spark.Request
import spark.Response
import spark.Spark.*

class UsersController(
    private val objectMapper: ObjectMapper,
    private val usersService: UsersService,
    private val pokedexService: PokedexService,
    private val sessionService: SessionService,
    private val subscriptionService: SubscriptionService,
) : Controller {
    override fun register() {
        path("/api/v1/users") {
            get("/:userId", APPLICATION_JSON, this::user, objectMapper::writeValueAsString)
            get("/:userId/pokedex/:dexId", APPLICATION_JSON, this::userDex, objectMapper::writeValueAsString)
            post(
                "/:userId/pokedex",
                APPLICATION_JSON,
                authenticated(this::createUserDex),
                objectMapper::writeValueAsString,
            )
            patch(
                "/:userId",
                APPLICATION_JSON,
                authenticated(this::updateUser),
                objectMapper::writeValueAsString,
            )
            post(
                "/:userId/subscriptions",
                APPLICATION_JSON,
                authenticated(this::subscribe),
                objectMapper::writeValueAsString,
            )
            delete(
                "/:userId/subscriptions/:subscriptionId",
                APPLICATION_JSON,
                authenticated(this::unsubscribe),
                objectMapper::writeValueAsString,
            )
        }
    }

    private fun user(req: Request, res: Response): User {
        val userId = req.paramNotNull(":userId")
        return usersService.findUserById(userId).orNotFound(userId)
    }

    private fun userDex(req: Request, res: Response): UserDex {
        val userId = req.paramNotNull(":userId")
        val dexId = req.paramNotNull(":dexId")

        res.header("Cache-Control", "no-cache")

        return usersService.findUserById(userId)
            .orNotFound(userId)
            .pokedex
            .find { it.userDexId == dexId }
            ?: throw NotFoundException("No dex with id $dexId found on user $userId")
    }

    private fun createUserDex(req: Request, res: Response, session: Session): UserDex {
        val creation: CreateDexDTO = objectMapper.readValue(req.body())
        val userDex = pokedexService.createUserDex(gameKey = creation.game, name = creation.name)

        usersService.createUserDex(userId = session.userId, userDex = userDex).orNotFound(session.userId)

        return userDex
    }

    private fun updateUser(req: Request, res: Response, session: Session): User {
        val changes: UpdateUserDTO = objectMapper.readValue(req.body())

        return usersService.updateUser(session.userId, changes).orNotFound(session.userId)
    }

    private fun subscribe(req: Request, res: Response, session: Session): User {
        val subscribe: SubscribeDTO = objectMapper.readValue(req.body())
        val user = usersService.findUserById(session.userId).orNotFound(session.userId)

        val subscriptions = if (!user.isSubscribed(userId = subscribe.userId, dexId = subscribe.dexId)) {
            val subscription = subscriptionService.subscribe(
                dexId = subscribe.dexId,
                userId = subscribe.userId,
                subscriberUserId = user.userId,
                token = subscribe.token,
            )

            user.subscriptions + subscription
        } else user.subscriptions

        return user.copy(subscriptions = subscriptions)
    }

    private fun unsubscribe(req: Request, res: Response, session: Session): User {
        val subscriptionId = req.params(":subscriptionId")
        val user = usersService.findUserById(session.userId).orNotFound(session.userId)

        if (user.isSubscribed(subscriptionId)) {
            subscriptionService.unsubscribe(subscriptionId)
        }

        return user.copy(
            subscriptions = user.subscriptions.filterNot { s -> s.subscriptionId == subscriptionId }.toSet(),
        )
    }

    private fun <T> authenticated(f: (Request, Response, Session) -> T): (Request, Response) -> T = { req, res ->
        val userId = req.paramNotNull(":userId")
        val dexToken = req.dexToken() ?: throw UnauthorizedException("Missing dex-token")

        val session = sessionService.findSession(dexToken)
            ?: throw UnauthorizedException("Session not found for given token")

        if (session.userId != userId) {
            throw ForbiddenException("You are not allowed")
        }

        f(req, res, session)
    }

    private fun User?.orNotFound(userId: String) = this ?: throw NotFoundException("No user found for user id $userId")
}