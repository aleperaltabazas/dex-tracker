package com.github.aleperaltabazas.dex.controller

import com.github.aleperaltabazas.dex.constants.DEX_TOKEN
import com.github.aleperaltabazas.dex.dto.dex.CreateDexDTO
import com.github.aleperaltabazas.dex.dto.dex.UpdateUserDTO
import com.github.aleperaltabazas.dex.exception.ForbiddenException
import com.github.aleperaltabazas.dex.exception.NotFoundException
import com.github.aleperaltabazas.dex.exception.UnauthorizedException
import com.github.aleperaltabazas.dex.model.User
import com.github.aleperaltabazas.dex.model.UserDex
import com.github.aleperaltabazas.dex.service.PokedexService
import com.github.aleperaltabazas.dex.service.SessionService
import com.github.aleperaltabazas.dex.service.UsersService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.CacheControl
import org.springframework.http.ResponseEntity
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
        usersService.findUserById(userId) ?: throw NotFoundException("No user found for user id $userId")

    @GetMapping("/{userId}/pokedex/{dexId}")
    fun userDex(
        @PathVariable userId: String,
        @PathVariable dexId: String,
    ): ResponseEntity<UserDex> {
        val user = usersService.findUserById(userId) ?: throw NotFoundException("No user found for user id $userId")
        val dex = user.pokedex.find { it.userDexId == dexId }
            ?: throw NotFoundException("No dex with id $dexId found on user $userId")

        return ResponseEntity.ok()
            .cacheControl(CacheControl.noCache())
            .body(dex)
    }

    @PostMapping("/{userId}/pokedex")
    fun createUserDex(
        @PathVariable userId: String,
        @RequestBody creation: CreateDexDTO,
        request: HttpServletRequest,
    ): UserDex {
        val session = authenticated(userId, request)
        val userDex = pokedexService.createUserDex(gameKey = creation.game, name = creation.name)
        usersService.createUserDex(userId = session.userId, userDex = userDex)
            ?: throw NotFoundException("No user found for user id $userId")
        return userDex
    }

    @PatchMapping("/{userId}")
    fun updateUser(
        @PathVariable userId: String,
        @RequestBody changes: UpdateUserDTO,
        request: HttpServletRequest,
    ): User {
        val session = authenticated(userId, request)
        return usersService.updateUser(session.userId, changes)
            ?: throw NotFoundException("No user found for user id $userId")
    }

    private fun authenticated(userId: String, request: HttpServletRequest) =
        (request.getHeader(DEX_TOKEN) ?: request.cookies?.find { it.name == DEX_TOKEN }?.value
            ?: throw UnauthorizedException("Missing dex-token"))
            .let { token ->
                sessionService.findSession(token) ?: throw UnauthorizedException("Session not found for given token")
            }
            .also { session ->
                if (session.userId != userId) throw ForbiddenException("You are not allowed")
            }
}
