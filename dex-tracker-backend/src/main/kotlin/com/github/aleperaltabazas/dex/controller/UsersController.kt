package com.github.aleperaltabazas.dex.controller

import com.github.aleperaltabazas.dex.constants.DEX_TOKEN
import com.github.aleperaltabazas.dex.dto.dex.CreateDexDTO
import com.github.aleperaltabazas.dex.dto.dex.UpdateUserDTO
import com.github.aleperaltabazas.dex.exception.ForbiddenException
import com.github.aleperaltabazas.dex.exception.NotFoundException
import com.github.aleperaltabazas.dex.exception.UnauthorizedException
import com.github.aleperaltabazas.dex.extension.dexToken
import com.github.aleperaltabazas.dex.model.Session
import com.github.aleperaltabazas.dex.model.User
import com.github.aleperaltabazas.dex.model.UserDex
import com.github.aleperaltabazas.dex.service.PokedexService
import com.github.aleperaltabazas.dex.service.SessionService
import com.github.aleperaltabazas.dex.service.UsersService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class UsersController(
    private val usersService: UsersService,
    private val pokedexService: PokedexService,
    private val sessionService: SessionService,
) {
    @GetMapping("/{userId}")
    fun user(@PathVariable userId: String): User =
        usersService.findUserById(userId).orNotFound(userId)

    @GetMapping("/{userId}/pokedex/{dexId}")
    fun userDex(
        @PathVariable userId: String,
        @PathVariable dexId: String,
        response: HttpServletResponse,
    ): UserDex {
        response.setHeader("Cache-Control", "no-cache")
        return usersService.findUserById(userId)
            .orNotFound(userId)
            .pokedex
            .find { it.userDexId == dexId }
            ?: throw NotFoundException("No dex with id $dexId found on user $userId")
    }

    @PostMapping("/{userId}/pokedex")
    fun createUserDex(
        @PathVariable userId: String,
        @RequestBody creation: CreateDexDTO,
        request: HttpServletRequest,
    ): UserDex {
        val session = authenticated(request, userId)
        val userDex = pokedexService.createUserDex(gameKey = creation.game, name = creation.name)
        usersService.createUserDex(userId = session.userId, userDex = userDex).orNotFound(session.userId)
        return userDex
    }

    @PatchMapping("/{userId}")
    fun updateUser(
        @PathVariable userId: String,
        @RequestBody changes: UpdateUserDTO,
        request: HttpServletRequest,
    ): User {
        val session = authenticated(request, userId)
        return usersService.updateUser(session.userId, changes).orNotFound(session.userId)
    }

    private fun authenticated(request: HttpServletRequest, userId: String): Session {
        val dexToken = request.dexToken() ?: throw UnauthorizedException("Missing dex-token")
        val session = sessionService.findSession(dexToken)
            ?: throw UnauthorizedException("Session not found for given token")
        if (session.userId != userId) throw ForbiddenException("You are not allowed")
        return session
    }

    private fun User?.orNotFound(userId: String) =
        this ?: throw NotFoundException("No user found for user id $userId")
}
