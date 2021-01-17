package com.github.aleperaltabazas.dex.mock

import arrow.core.Right
import com.fasterxml.jackson.core.type.TypeReference
import com.github.aleperaltabazas.dex.connector.HttpResponse
import com.github.aleperaltabazas.dex.connector.RestConnector
import com.github.aleperaltabazas.dex.dto.pokeapi.PokedexDTO
import com.github.aleperaltabazas.dex.dto.pokeapi.PokemonEntryDTO
import com.github.aleperaltabazas.dex.dto.pokeapi.RegionDTO
import com.github.aleperaltabazas.dex.dto.pokeapi.SpeciesRefDTO
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.mockito.ArgumentMatchers

object PokeapiConnector {
    fun kantoPokedex(pokeapiMock: RestConnector) {
        val responseMock: HttpResponse = mock {
            on { deserializeAs(ArgumentMatchers.any(TypeReference::class.java)) }.thenReturn(
                PokedexDTO(
                    name = "kanto",
                    region = RegionDTO(
                        name = "kanto"
                    ),
                    pokemonEntries = listOf(
                        PokemonEntryDTO(
                            entryNumber = 2,
                            pokemonSpecies = SpeciesRefDTO(name = "ivysaur")
                        ),
                        PokemonEntryDTO(
                            entryNumber = 3,
                            pokemonSpecies = SpeciesRefDTO(name = "venusaur")
                        ),
                        PokemonEntryDTO(
                            entryNumber = 133,
                            pokemonSpecies = SpeciesRefDTO(name = "eevee")
                        ),
                    )
                )
            )
        }

        whenever(pokeapiMock.get("/api/v2/pokedex/kanto")).thenReturn(Right(responseMock))
    }
}