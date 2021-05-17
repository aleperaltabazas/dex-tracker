package com.github.aleperaltabazas.dex.controller

import arrow.core.left
import arrow.core.right
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.constants.APPLICATION_JSON
import com.github.aleperaltabazas.dex.model.Pokemon
import com.github.aleperaltabazas.dex.service.PokemonService
import spark.Request
import spark.Response
import spark.Spark.get

class PokemonController(
    private val pokemonService: PokemonService,
    private val objectMapper: ObjectMapper,
) : Controller {
    override fun register() {
        get(
            "/api/v1/games/:game/pokemon/:key",
            APPLICATION_JSON,
            this::pokemon,
            objectMapper::writeValueAsString,
        )
    }

    private fun pokemon(req: Request, res: Response): Pokemon {
        val game = req.params(":game")
        val key = req.params(":key")

        return pokemonService.pokemon(
            game,
            key.toIntOrNull()?.left() ?: key.right(),
        )
    }
}