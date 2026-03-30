package com.github.aleperaltabazas.dex.controller

import arrow.core.left
import arrow.core.right
import com.github.aleperaltabazas.dex.model.Pokemon
import com.github.aleperaltabazas.dex.service.PokemonService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/games")
class PokemonController(
    private val pokemonService: PokemonService,
) {
    @GetMapping("/{game}/pokemon/{key}")
    fun pokemon(
        @PathVariable game: String,
        @PathVariable key: String,
    ): Pokemon = pokemonService.pokemon(
        game,
        key.toIntOrNull()?.left() ?: key.right(),
    )
}
