package com.github.aleperaltabazas.dex.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.constants.APPLICATION_JSON
import com.github.aleperaltabazas.dex.model.Pokedex
import com.github.aleperaltabazas.dex.service.PokedexService
import spark.Request
import spark.Response
import spark.Spark.get
import spark.Spark.path

class PokedexController(
    private val objectMapper: ObjectMapper,
    private val pokedexService: PokedexService,
) : Controller {
    override fun register() {
        path("/api/v1/pokedex") {
            get("", APPLICATION_JSON, this::allPokedex, objectMapper::writeValueAsString)
        }
    }

    private fun allPokedex(req: Request, res: Response): List<Pokedex> = pokedexService.allPokedex()
}