package com.github.aleperaltabazas.dex.service

import com.fasterxml.jackson.core.type.TypeReference
import com.github.aleperaltabazas.dex.cache.pokedex.GamePokedexCache
import com.github.aleperaltabazas.dex.dto.dex.DexEntryDTO
import com.github.aleperaltabazas.dex.dto.dex.GameDTO
import com.github.aleperaltabazas.dex.dto.dex.GamePokedexDTO
import com.github.aleperaltabazas.dex.exception.NotFoundException
import com.github.aleperaltabazas.dex.model.Game
import com.github.aleperaltabazas.dex.model.GamePokedex
import com.github.aleperaltabazas.dex.model.PokedexType
import com.github.aleperaltabazas.dex.model.Pokemon
import com.github.aleperaltabazas.dex.storage.Query
import com.github.aleperaltabazas.dex.storage.Storage
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldThrow
import io.kotlintest.specs.WordSpec
import org.mockito.ArgumentMatchers.any as anyClass

class PokemonServiceTest : WordSpec() {
    init {
        val cacheMock: GamePokedexCache = mock {}
        val storageMock: Storage = mock {}
        val pokemonService = PokemonService(
            gamePokedexCache = cacheMock,
            storage = storageMock
        )

        "gameNationalPokedex" should {
            "find the game associated to the key in the cache and make query the storage for that game's generation" {
                whenever(cacheMock.get()).thenReturn(
                    mapOf(
                        "gsc" to GamePokedex(
                            type = PokedexType.NATIONAL,
                            game = Game(
                                title = "gsc",
                                fullTitle = "Gold, Silver and Crystal",
                                region = "johto",
                                pokeapiId = "original-johto",
                                spritePokemon = "ho-oh",
                                gen = 2,
                            ),
                            pokemon = emptyList()
                        )
                    )
                )


                val queryMock: Query = mock {
                    on { this.where(any()) }.thenReturn(it)
                    on { this.findAll(anyClass(TypeReference::class.java)) }.thenReturn(
                        listOf(
                            Pokemon(
                                name = "pichu",
                                nationalPokedexNumber = 172,
                                primaryAbility = "static",
                                secondaryAbility = null,
                                hiddenAbility = "lightning-rod",
                                evolutions = emptyList(),
                                forms = emptyList(),
                                gen = 2,
                            )
                        )
                    )
                }

                whenever(storageMock.query(any())).thenReturn(queryMock)

                val expected = GamePokedexDTO(
                    region = "johto",
                    type = PokedexType.NATIONAL,
                    game = GameDTO(
                        title = "gsc",
                        fullTitle = "Gold, Silver and Crystal",
                        spritePokemon = "ho-oh"
                    ),
                    pokemon = listOf(
                        DexEntryDTO(
                            name = "pichu",
                            number = 172,
                            forms = emptyList(),
                        )
                    )
                )

                val actual = pokemonService.gameNationalPokedex("gsc")

                actual shouldBe expected
            }

            "throw a NotFoundException if no game in the cache matches the key" {
                whenever(cacheMock.get()).thenReturn(
                    mapOf(
                        "rse" to GamePokedex(
                            type = PokedexType.NATIONAL,
                            game = Game(
                                title = "rse",
                                fullTitle = "Ruby, Sapphire and Emerald",
                                region = "hoenn",
                                pokeapiId = "original-hoenn",
                                spritePokemon = "rayquaza",
                                gen = 3,
                            ),
                            pokemon = emptyList()
                        )
                    )
                )

                val queryMock: Query = mock {
                    on { this.where(any()) }.thenReturn(it)
                    on { this.findAll(anyClass(TypeReference::class.java)) }.thenReturn(
                        listOf(
                            Pokemon(
                                name = "pichu",
                                nationalPokedexNumber = 172,
                                primaryAbility = "static",
                                secondaryAbility = null,
                                hiddenAbility = "lightning-rod",
                                evolutions = emptyList(),
                                forms = emptyList(),
                                gen = 3,
                            )
                        )
                    )
                }

                whenever(storageMock.query(any())).thenReturn(queryMock)

                shouldThrow<NotFoundException> {
                    pokemonService.gameNationalPokedex("gsc")
                }
            }
        }
    }
}