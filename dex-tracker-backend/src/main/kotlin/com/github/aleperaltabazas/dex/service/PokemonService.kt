package com.github.aleperaltabazas.dex.service

import com.github.aleperaltabazas.dex.cache.pokedex.RegionalPokedexCache
import com.github.aleperaltabazas.dex.db.schema.PokemonTable
import com.github.aleperaltabazas.dex.exception.NotFoundException
import com.github.aleperaltabazas.dex.model.Pokemon
import com.github.aleperaltabazas.dex.storage.PokemonStorage
import org.jetbrains.exposed.sql.and

class PokemonService(
    private val regionalPokedexCache: RegionalPokedexCache,
    private val pokemonStorage: PokemonStorage,
) {
    fun generationNationalPokedex(gen: Int) = pokemonStorage.findAll { PokemonTable.gen eq gen }

    fun gameRegionalPokedex(gameKey: String): List<Pokemon> {
        val (game, pokedex) = regionalPokedexCache.get()
            .toList()
            .find { (g, _) -> g.title == gameKey }
            ?: throw NotFoundException("No game found for key $gameKey")

        return pokemonStorage.findAll { PokemonTable.gen eq game.gen and PokemonTable.name.inList(pokedex.pokemon) }
    }

    fun nationalPokedex() = pokemonStorage.findAll {
        PokemonTable.gen eq null
    }
}