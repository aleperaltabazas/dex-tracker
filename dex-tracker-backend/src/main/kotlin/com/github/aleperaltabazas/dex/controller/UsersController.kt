package com.github.aleperaltabazas.dex.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.aleperaltabazas.dex.constants.APPLICATION_JSON
import com.github.aleperaltabazas.dex.constants.DEX_TOKEN
import com.github.aleperaltabazas.dex.dto.dex.CaughtStatusDTO
import com.github.aleperaltabazas.dex.dto.dex.CreateUserDexDTO
import com.github.aleperaltabazas.dex.dto.dex.UserDTO
import com.github.aleperaltabazas.dex.dto.dex.UserDexDTO
import com.github.aleperaltabazas.dex.exception.BadRequestException
import com.github.aleperaltabazas.dex.exception.UnauthorizedException
import com.github.aleperaltabazas.dex.mapper.ModelMapper
import com.github.aleperaltabazas.dex.service.PokedexService
import com.github.aleperaltabazas.dex.service.UsersService
import org.slf4j.LoggerFactory
import spark.Request
import spark.Response
import spark.Spark.*

class UsersController(
    private val objectMapper: ObjectMapper,
    private val usersService: UsersService,
    private val modelMapper: ModelMapper,
    private val pokedexService: PokedexService,
) : Controller {
    override fun register() {
            path("/api/v1/users") {
            get("", APPLICATION_JSON, this::findUser, objectMapper::writeValueAsString)
            get("/pokedex", APPLICATION_JSON, this::usersPokedex, objectMapper::writeValueAsString)
            post("/pokedex", APPLICATION_JSON, this::createUserDex, objectMapper::writeValueAsString)
            get("/pokedex/:id", APPLICATION_JSON, this::findUserDex, objectMapper::writeValueAsString)
            patch("/pokedex", APPLICATION_JSON, this::updateUserDexCaughtStatus, objectMapper::writeValueAsString)
        }
    }

    private fun findUserDex(req: Request, res: Response): UserDexDTO {
        val token = requireNotNull(req.cookie(DEX_TOKEN)) {
            throw UnauthorizedException("No dex-token found")
        }

        val dexId = requireNotNull(req.params(":id")) {
            throw BadRequestException(":id path param should not be null")
        }

        return modelMapper.mapUserDexToDTO(usersService.findUserDex(token, dexId))
    }

    private fun createUserDex(req: Request, res: Response): UserDexDTO {
        val token = req.cookie(DEX_TOKEN)

        val body: CreateUserDexDTO = objectMapper.readValue(req.body())
        val pokedex = pokedexService.createUserDex(body)
        token?.let { usersService.createUserDex(it, pokedex) }

        return modelMapper.mapUserDexToDTO(pokedex)
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

        return usersService.unsafeFindUserByToken(dexToken).let { modelMapper.mapUserToDTO(it) }
    }

    private fun usersPokedex(req: Request, res: Response): List<UserDexDTO> {
        val dexToken = requireNotNull(req.cookie(DEX_TOKEN)) {
            throw BadRequestException("User has no dex-token stored")
        }

        return usersService.unsafeFindUserByToken(dexToken)
            .pokedex
            .let { dex -> dex.map { modelMapper.mapUserDexToDTO(it) } }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(UsersController::class.java)
    }
}