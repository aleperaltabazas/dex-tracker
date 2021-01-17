package com.github.aleperaltabazas.dex.service

import com.github.aleperaltabazas.dex.cache.pokedex.GamePokedexCache
import com.github.aleperaltabazas.dex.db.schema.PokemonTable
import com.github.aleperaltabazas.dex.dto.dex.DexEntryDTO
import com.github.aleperaltabazas.dex.dto.dex.FormDTO
import com.github.aleperaltabazas.dex.dto.dex.GameDTO
import com.github.aleperaltabazas.dex.dto.dex.GamePokedexDTO
import com.github.aleperaltabazas.dex.exception.NotFoundException
import com.github.aleperaltabazas.dex.model.GamePokedex
import com.github.aleperaltabazas.dex.model.PokedexType
import com.github.aleperaltabazas.dex.storage.PokemonStorage
import org.jetbrains.exposed.sql.and

open class PokemonService(
    private val gamePokedexCache: GamePokedexCache,
    private val pokemonStorage: PokemonStorage,
) {
    open fun gameNationalPokedex(gameKey: String): GamePokedexDTO {
        val pokedex = gameFromKey(gameKey)
        val pokemon = pokemonStorage.findAll { PokemonTable.gen eq pokedex.game.gen }
            .map {
                DexEntryDTO(
                    name = it.name,
                    number = it.nationalPokedexNumber,
                    forms = it.forms.map { f -> FormDTO(f) }
                )
            }

        return GamePokedexDTO(
            pokemon = pokemon.toList(),
            type = PokedexType.NATIONAL,
            region = pokedex.game.region,
            game = GameDTO(pokedex.game)
        )
    }

    open fun gameRegionalPokedex(gameKey: String): GamePokedexDTO {
        val pokedex = gameFromKey(gameKey)

        val pokemon = pokemonStorage
            .findAll { (PokemonTable.gen eq pokedex.game.gen) and (PokemonTable.name inList pokedex.pokemon) }
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
            type = PokedexType.REGIONAL,
            region = pokedex.game.region,
            game = GameDTO(pokedex.game)
        )
    }

    private fun gameFromKey(gameKey: String): GamePokedex = gamePokedexCache.get()
        .toList()
        .find { (g, _) -> g == gameKey }
        ?.second
        ?: throw NotFoundException("No game found for key $gameKey")
}