package com.github.aleperaltabazas.dex.controller

import arrow.core.left
import arrow.core.right
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.constants.APPLICATION_JSON
import com.github.aleperaltabazas.dex.dto.dex.GamePokedexDTO
import com.github.aleperaltabazas.dex.exception.BadRequestException
import com.github.aleperaltabazas.dex.mapper.ModelMapper
import com.github.aleperaltabazas.dex.model.Pokemon
import com.github.aleperaltabazas.dex.service.PokedexService
import org.slf4j.LoggerFactory
import spark.Request
import spark.Response
import spark.Spark.get
import spark.Spark.path

class PokedexController(
    private val objectMapper: ObjectMapper,
    private val pokedexService: PokedexService,
    private val modelMapper: ModelMapper,
) : Controller {
    override fun register() {
        path("/api/v1/pokedex") {
            get("", APPLICATION_JSON, this::allPokedex, objectMapper::writeValueAsString)
            get("/:game/:type", APPLICATION_JSON, this::gamePokedex, objectMapper::writeValueAsString)
            get("/:game/pokemon/:id", APPLICATION_JSON, this::pokemon, objectMapper::writeValueAsString)
        }
    }

    private fun allPokedex(req: Request, res: Response) = pokedexService.allPokedex()

    private fun gamePokedex(req: Request, res: Response): GamePokedexDTO {
        val game = requireNotNull(req.params(":game")) {
            throw BadRequestException(":game path variable should not be null")
        }
        val type = requireNotNull(req.params(":type")) {
            throw BadRequestException("Path variable :type should not be null")
        }

        return when (type.toLowerCase()) {
            "regional" -> pokedexService.gameRegionalPokedex(game).let { modelMapper.mapGamePokedexToDTO(it) }
            "national" -> pokedexService.gameNationalPokedex(game).let { modelMapper.mapGamePokedexToDTO(it) }
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
            pokedexService.pokemon(gameKey = game, numberOrName = it.left())
        } ?: pokedexService.pokemon(game, numberOrName = numberOrName.right())
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(PokedexController::class.java)
    }
}