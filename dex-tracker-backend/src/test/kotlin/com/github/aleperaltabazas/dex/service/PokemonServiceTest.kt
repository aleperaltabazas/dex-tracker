package com.github.aleperaltabazas.dex.service

import arrow.core.left
import arrow.core.right
import com.fasterxml.jackson.core.type.TypeReference
import com.github.aleperaltabazas.dex.cache.pokedex.RegionalPokedexCache
import com.github.aleperaltabazas.dex.db.fixture.ivysaur
import com.github.aleperaltabazas.dex.exception.NotFoundException
import com.github.aleperaltabazas.dex.model.Game
import com.github.aleperaltabazas.dex.model.GamePokedex
import com.github.aleperaltabazas.dex.model.PokedexType
import com.github.aleperaltabazas.dex.storage.Collection
import com.github.aleperaltabazas.dex.storage.Query
import com.github.aleperaltabazas.dex.storage.Storage
import com.nhaarman.mockito_kotlin.*
import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldThrow
import io.kotlintest.specs.WordSpec
import org.bson.Document
import org.mockito.ArgumentMatchers

class PokemonServiceTest : WordSpec() {
    init {
        val cacheMock: RegionalPokedexCache = mock {}
        val storageMock: Storage = mock {}
        val gameServiceMock: GameService = mock {}
        val pokemonService = PokemonService(
            regionalPokedexCache = cacheMock,
            storage = storageMock,
            gameService = gameServiceMock
        )

        "pokemon" should {
            val goldAndSilver = Game(
                title = "gs",
                fullTitle = "Gold and Silver",
                spritePokemon = "Ho-oh",
                region = "Johto",
                pokeapiId = "123",
                gen = 2
            )

            val goldAndSilverPokedex = GamePokedex(
                game = goldAndSilver,
                type = PokedexType.NATIONAL,
                pokemon = emptyList()
            )

            "find a pokemon by its number" {
                val queryMock: Query = mock {
                    on { this.findOne(ArgumentMatchers.any(TypeReference::class.java)) } doReturn ivysaur
                }
                whenever(queryMock.where(any())).thenReturn(queryMock)
                whenever(queryMock.limit(any())).thenReturn(queryMock)
                whenever(cacheMock.pokedexOf(any())).thenReturn(goldAndSilverPokedex)
                whenever(gameServiceMock.gameFromKey(any())).thenReturn(goldAndSilver)
                whenever(storageMock.query(any())).thenReturn(queryMock)

                val expected = ivysaur
                val actual = pokemonService.pokemon("gs", 2.left())

                actual shouldBe expected

                verify(queryMock).where(eq(Document("gen", goldAndSilver.gen).append("national_pokedex_number", 2)))
                verify(queryMock).limit(eq(1))
                verify(storageMock).query(eq(Collection.POKEMON))
                verify(gameServiceMock).gameFromKey(eq("gs"))
                verify(cacheMock).pokedexOf(eq(goldAndSilver))
            }

            "find a pokemon by its name" {
                val queryMock: Query = mock {
                    on { this.findOne(ArgumentMatchers.any(TypeReference::class.java)) } doReturn ivysaur
                }
                whenever(queryMock.where(any())).thenReturn(queryMock)
                whenever(queryMock.limit(any())).thenReturn(queryMock)
                whenever(cacheMock.pokedexOf(any())).thenReturn(goldAndSilverPokedex)
                whenever(gameServiceMock.gameFromKey(any())).thenReturn(goldAndSilver)
                whenever(storageMock.query(any())).thenReturn(queryMock)

                val expected = ivysaur
                val actual = pokemonService.pokemon("gs", "ivysaur".right())

                actual shouldBe expected

                verify(queryMock).where(eq(Document("gen", goldAndSilver.gen).append("name", "ivysaur")))
                verify(queryMock).limit(eq(1))
                verify(storageMock).query(eq(Collection.POKEMON))
                verify(gameServiceMock).gameFromKey(eq("gs"))
                verify(cacheMock).pokedexOf(eq(goldAndSilver))
            }

            "throw a NotFoundException if no pokemon matches either criteria" {
                val queryMock: Query = mock {
                    on { this.findOne(ArgumentMatchers.any(TypeReference::class.java)) } doReturn null
                }
                whenever(queryMock.where(any())).thenReturn(queryMock)
                whenever(queryMock.limit(any())).thenReturn(queryMock)
                whenever(cacheMock.pokedexOf(any())).thenReturn(goldAndSilverPokedex)
                whenever(gameServiceMock.gameFromKey(any())).thenReturn(goldAndSilver)
                whenever(storageMock.query(any())).thenReturn(queryMock)

                shouldThrow<NotFoundException> {
                    pokemonService.pokemon("gs", "ivysaur".right())
                }

                verify(queryMock).where(eq(Document("gen", goldAndSilver.gen).append("name", "ivysaur")))

                shouldThrow<NotFoundException> {
                    pokemonService.pokemon("gs", 2.left())
                }

                verify(queryMock).where(eq(Document("gen", goldAndSilver.gen).append("national_pokedex_number", 2)))
            }
        }
    }
}