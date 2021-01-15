package com.github.aleperaltabazas.dex.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.dto.dex.GamePokedexDTO
import com.github.aleperaltabazas.dex.exception.BadRequestException
import com.github.aleperaltabazas.dex.service.PokemonService
import org.slf4j.LoggerFactory
import spark.Request
import spark.Response
import spark.Spark.*

class PokedexController(
    private val objectMapper: ObjectMapper,
    private val pokemonService: PokemonService,
) : Controller {
    override fun register() {
        path("/api/v1") {
            before("/pokedex/:game/:type") { req, _ ->
                LOGGER.info("[REQUEST] ${req.requestMethod()} ${req.contextPath()}")
                LOGGER.info("Game: ${req.params(":game")} - Type ${req.params(":type")}")
            }
            after("/pokedex/:game/:type") { _, res ->
                LOGGER.info("[RESPONSE] ${res.status()}")
            }
            get("/pokedex/:game/:type", "application/json", this::gamePokedex, objectMapper::writeValueAsString)
        }
    }

    private fun gamePokedex(req: Request, res: Response): GamePokedexDTO {
        val game = requireNotNull(req.params(":game")) {
            throw BadRequestException(":game path variable should not be null")
        }
        val type = requireNotNull(req.params(":type")) {
            throw BadRequestException("Path variable :type should not be null")
        }

        return when (type.toLowerCase()) {
            "regional" -> pokemonService.gameRegionalPokedex(game)
            "national" -> pokemonService.gameNationalPokedex(game)
            else -> throw BadRequestException("Unsupported pokedex type $type for game $game")
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(PokedexController::class.java)
    }
}