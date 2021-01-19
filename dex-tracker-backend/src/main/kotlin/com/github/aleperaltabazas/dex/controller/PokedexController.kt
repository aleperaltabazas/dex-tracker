package com.github.aleperaltabazas.dex.controller

import arrow.core.left
import arrow.core.right
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.constants.APPLICATION_JSON
import com.github.aleperaltabazas.dex.dto.dex.GamePokedexDTO
import com.github.aleperaltabazas.dex.exception.BadRequestException
import com.github.aleperaltabazas.dex.extension.prettyHeaders
import com.github.aleperaltabazas.dex.model.Pokemon
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
        path("/api/v1/pokedex") {
            get("", APPLICATION_JSON, this::allPokedex, objectMapper::writeValueAsString)
            get("/:game/:type", APPLICATION_JSON, this::gamePokedex, objectMapper::writeValueAsString)
            get("/:game/pokemon/:id", APPLICATION_JSON, this::pokemon, objectMapper::writeValueAsString)

            before("/*") { req, res ->
                LOGGER.info("[${req.requestMethod()}] ${req.contextPath()} Request headers: ${req.prettyHeaders()}")
            }
            after("/*") { _, res ->
                LOGGER.info("Response: ${res.status()}")
            }
        }
    }

    private fun allPokedex(req: Request, res: Response) = pokemonService.allPokedex()

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

    private fun pokemon(req: Request, res: Response): Pokemon {
        val game = requireNotNull(req.params(":game")) {
            throw BadRequestException(":game path variable should not be null")
        }

        val numberOrName = requireNotNull(req.params(":id")) {
            throw BadRequestException(":id path variable should not be null")
        }

        return numberOrName.toIntOrNull()?.let {
            pokemonService.pokemon(gameKey = game, numberOrName = it.left())
        } ?: pokemonService.pokemon(game, numberOrName = numberOrName.right())
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(PokedexController::class.java)
    }
}