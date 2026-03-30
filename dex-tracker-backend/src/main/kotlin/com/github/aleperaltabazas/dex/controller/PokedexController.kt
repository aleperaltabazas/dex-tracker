package com.github.aleperaltabazas.dex.controller

import com.github.aleperaltabazas.dex.model.Pokedex
import com.github.aleperaltabazas.dex.service.PokedexService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/pokedex")
class PokedexController(
    private val pokedexService: PokedexService,
) {
    @GetMapping
    fun allPokedex(): List<Pokedex> = pokedexService.allPokedex()
}
