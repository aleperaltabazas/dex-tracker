package com.github.aleperaltabazas.dex.service

import com.github.aleperaltabazas.dex.cache.pokedex.RegionalPokedexCache
import com.github.aleperaltabazas.dex.db.schema.PokemonTable
import com.github.aleperaltabazas.dex.dto.dex.DexEntryDTO
import com.github.aleperaltabazas.dex.dto.dex.FormDTO
import com.github.aleperaltabazas.dex.dto.dex.GameDTO
import com.github.aleperaltabazas.dex.dto.dex.GamePokedexDTO
import com.github.aleperaltabazas.dex.exception.NotFoundException
import com.github.aleperaltabazas.dex.model.Game
import com.github.aleperaltabazas.dex.model.GamePokedex
import com.github.aleperaltabazas.dex.model.PokedexType
import com.github.aleperaltabazas.dex.storage.PokemonStorage
import org.jetbrains.exposed.sql.and

class PokemonService(
    private val regionalPokedexCache: RegionalPokedexCache,
    private val pokemonStorage: PokemonStorage,
) {
    fun gameNationalPokedex(gameKey: String): GamePokedexDTO {
        val (game, _) = gameFromKey(gameKey)
        val pokemon = pokemonStorage.findAll { PokemonTable.gen eq game.gen }
            .map {
                DexEntryDTO(
                    name = it.name,
                    number = it.nationalPokedexNumber,
                    forms = it.forms.map { f -> FormDTO(f) }
                )
            }

        return GamePokedexDTO(
            pokemon = pokemon.toList(),
            type = PokedexType.NATIONAL.name,
            region = game.region,
            game = GameDTO(game)
        )
    }

    fun gameRegionalPokedex(gameKey: String): GamePokedexDTO {
        val (game, pokedex) = gameFromKey(gameKey)

        val pokemon = pokemonStorage
            .findAll { (PokemonTable.gen eq game.gen) and (PokemonTable.name inList pokedex.pokemon) }
            .map {
                DexEntryDTO(
                    number = pokedex.pokemon.indexOf(it.name),
                    name = it.name,
                    forms = it.forms.map { f -> FormDTO(f) }
                )
            }
            .toList()

        return GamePokedexDTO(
            pokemon = pokemon.toList(),
            type = PokedexType.REGIONAL.name,
            region = game.region,
            game = GameDTO(game)
        )
    }

    private fun gameFromKey(gameKey: String): Pair<Game, GamePokedex> {
        return (regionalPokedexCache.get()
            .toList()
            .find { (g, _) -> g.title == gameKey }
            ?: throw NotFoundException("No game found for key $gameKey"))
    }
}