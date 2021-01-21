package com.github.aleperaltabazas.dex.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.constants.APPLICATION_JSON
import com.github.aleperaltabazas.dex.service.GameService
import spark.Request
import spark.Response
import spark.Spark.get
import spark.Spark.path

class GameController(
    private val objectMapper: ObjectMapper,
    private val gameService: GameService,
) : Controller {
    override fun register() {
        path("/api/v1/games") {
            get("", APPLICATION_JSON, this::all, objectMapper::writeValueAsString)
        }
    }

    private fun all(req: Request, res: Response) = gameService.all()
}