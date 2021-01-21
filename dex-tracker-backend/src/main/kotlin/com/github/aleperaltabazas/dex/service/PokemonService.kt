package com.github.aleperaltabazas.dex.service

import arrow.core.Either
import com.fasterxml.jackson.core.type.TypeReference
import com.github.aleperaltabazas.dex.cache.pokedex.RegionalPokedexCache
import com.github.aleperaltabazas.dex.dto.dex.DexEntryDTO
import com.github.aleperaltabazas.dex.dto.dex.FormDTO
import com.github.aleperaltabazas.dex.dto.dex.GameDTO
import com.github.aleperaltabazas.dex.dto.dex.GamePokedexDTO
import com.github.aleperaltabazas.dex.exception.NotFoundException
import com.github.aleperaltabazas.dex.model.Game
import com.github.aleperaltabazas.dex.model.PokedexType
import com.github.aleperaltabazas.dex.model.Pokemon
import com.github.aleperaltabazas.dex.storage.Collection
import com.github.aleperaltabazas.dex.storage.Storage
import org.bson.Document

open class PokemonService(
    private val gameService: GameService,
    private val regionalPokedexCache: RegionalPokedexCache,
    private val storage: Storage,
) {
    open fun allPokedex(): List<GamePokedexDTO> {
        val nationals = gameService.all().map { gameNationalPokedex(it) }
        val regionals = gameService.all().map { gameRegionalPokedex(it) }

        return regionals + nationals
    }

    open fun pokemon(gameKey: String, numberOrName: Either<Int, String>) = gameService.gameFromKey(gameKey)
        .let { regionalPokedexCache.pokedexOf(it) }
        .let {
            val gen = it.game.gen
            numberOrName.fold(
                ifLeft = {
                    storage.query(Collection.POKEMON)
                        .where(Document("gen", gen).append("national_pokedex_number", it))
                        .limit(1)
                        .findAll(POKEMON_REF)
                        .firstOrNull()
                },
                ifRight = {
                    storage.query(Collection.POKEMON)
                        .where(Document("gen", gen).append("name", it))
                        .limit(1)
                        .findAll(POKEMON_REF)
                        .firstOrNull()
                },
            )
        } ?: throw NotFoundException("No pokemon found with ${numberOrName.fold({ "number $it" }, { "name $it" })}")

    open fun gameNationalPokedex(gameKey: String): GamePokedexDTO = gameNationalPokedex(
        game = gameService.gameFromKey(gameKey)
    )

    private fun gameNationalPokedex(game: Game): GamePokedexDTO {
        val pokemon = storage.query(Collection.POKEMON)
            .where(Document("gen", game.gen))
            .findAll(POKEMON_REF)
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
            region = game.region,
            game = GameDTO(game)
        )
    }

    open fun gameRegionalPokedex(gameKey: String): GamePokedexDTO = gameRegionalPokedex(
        game = gameService.gameFromKey(gameKey)
    )

    private fun gameRegionalPokedex(game: Game): GamePokedexDTO {
        val pokedex = regionalPokedexCache.pokedexOf(game)

        val pokemon = storage
            .query(Collection.POKEMON)
            .where(Document("gen", game.gen))
            .findAll(POKEMON_REF)
            .filter { it.name in pokedex.pokemon }
            .mapIndexed { idx, p ->
                DexEntryDTO(
                    number = idx + 1,
                    name = p.name,
                    forms = p.forms.map { f -> FormDTO(f) }
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

    companion object {
        private val POKEMON_REF = object : TypeReference<Pokemon>() {}
    }
}