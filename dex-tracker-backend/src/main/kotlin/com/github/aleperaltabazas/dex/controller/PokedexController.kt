package com.github.aleperaltabazas.dex.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.dto.dex.GamePokedexDTO
import com.github.aleperaltabazas.dex.exception.BadRequestException
import com.github.aleperaltabazas.dex.service.PokemonService
import spark.Request
import spark.Response
import spark.Spark.get
import spark.Spark.path

class PokedexController(
    private val objectMapper: ObjectMapper,
    private val pokemonService: PokemonService,
) : Controller {
    override fun register() {
        path("/api/v1") {
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
}