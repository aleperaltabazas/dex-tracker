package com.github.aleperaltabazas.dex.service

import arrow.core.left
import arrow.core.right
import com.fasterxml.jackson.core.type.TypeReference
import com.github.aleperaltabazas.dex.cache.pokedex.RegionalPokedexCache
import com.github.aleperaltabazas.dex.db.fixture.bulbasaur
import com.github.aleperaltabazas.dex.db.fixture.ivysaur
import com.github.aleperaltabazas.dex.db.fixture.venusaur
import com.github.aleperaltabazas.dex.dto.dex.DexEntryDTO
import com.github.aleperaltabazas.dex.dto.dex.GameDTO
import com.github.aleperaltabazas.dex.dto.dex.GamePokedexDTO
import com.github.aleperaltabazas.dex.exception.NotFoundException
import com.github.aleperaltabazas.dex.model.Game
import com.github.aleperaltabazas.dex.model.GamePokedex
import com.github.aleperaltabazas.dex.model.PokedexType
import com.github.aleperaltabazas.dex.storage.Collection
import com.github.aleperaltabazas.dex.storage.Query
import com.github.aleperaltabazas.dex.storage.Storage
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
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
                val queryMock: Query = mockQuery(ivysaur) {
                    this.findOne(ArgumentMatchers.any(TypeReference::class.java))
                }

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
                val queryMock = mockQuery(ivysaur) {
                    this.findOne(ArgumentMatchers.any(TypeReference::class.java))
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
                val queryMock: Query = mockQuery(null) {
                    this.findOne(ArgumentMatchers.any(TypeReference::class.java))
                }
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

        "gameNationalPokedex" should {
            val pokedex = GamePokedexDTO(
                region = "johto",
                type = PokedexType.NATIONAL,
                game = GameDTO(
                    title = "gs",
                    fullTitle = "Gold and Silver",
                    spritePokemon = "ho-oh"
                ),
                pokemon = listOf(
                    DexEntryDTO(
                        name = "bulbasaur",
                        number = 1,
                        forms = emptyList(),
                    ),
                    DexEntryDTO(
                        name = "ivysaur",
                        number = 2,
                        forms = emptyList(),
                    ),
                    DexEntryDTO(
                        name = "venusaur",
                        number = 3,
                        forms = emptyList(),
                    ),
                )
            )

            val goldAndSilver = Game(
                title = "gs",
                fullTitle = "Gold and Silver",
                spritePokemon = "ho-oh",
                region = "johto",
                pokeapiId = "123",
                gen = 2
            )

            "return the national pokedex for Gold and Silver" {
                val queryMock = mockQuery(listOf(bulbasaur, ivysaur, venusaur)) {
                    this.findAll(ArgumentMatchers.any(TypeReference::class.java))
                }

                whenever(gameServiceMock.gameFromKey(any())).thenReturn(goldAndSilver)
                whenever(storageMock.query(any())).thenReturn(queryMock)

                val expected = pokedex
                val actual = pokemonService.gameNationalPokedex("hgss")

                actual shouldBe expected

                verify(queryMock).where(eq(Document("gen", 2)))
                verify(queryMock).sort(eq(Sorts.ascending("national_pokedex_number")))
                verify(gameServiceMock).gameFromKey(eq("hgss"))
            }
        }

        "gameRegionalPokedex" should {
            val pokedex = GamePokedexDTO(
                region = "johto",
                type = PokedexType.REGIONAL,
                game = GameDTO(
                    title = "gs",
                    fullTitle = "Gold and Silver",
                    spritePokemon = "ho-oh"
                ),
                pokemon = listOf(
                    DexEntryDTO(
                        name = "bulbasaur",
                        number = 1,
                        forms = emptyList(),
                    ),
                    DexEntryDTO(
                        name = "ivysaur",
                        number = 2,
                        forms = emptyList(),
                    ),
                    DexEntryDTO(
                        name = "venusaur",
                        number = 3,
                        forms = emptyList(),
                    ),
                )
            )

            val goldAndSilver = Game(
                title = "gs",
                fullTitle = "Gold and Silver",
                spritePokemon = "ho-oh",
                region = "johto",
                pokeapiId = "123",
                gen = 2
            )

            val goldAndSilverPokedex = GamePokedex(
                game = goldAndSilver,
                type = PokedexType.NATIONAL,
                pokemon = listOf("bulbasaur", "ivysaur", "venusaur")
            )

            "return the national pokedex for Gold and Silver" {
                val queryMock = mockQuery(listOf(ivysaur, bulbasaur, venusaur)) {
                    this.findAll(ArgumentMatchers.any(TypeReference::class.java))
                }

                whenever(cacheMock.pokedexOf(any())).thenReturn(goldAndSilverPokedex)
                whenever(gameServiceMock.gameFromKey(any())).thenReturn(goldAndSilver)
                whenever(storageMock.query(any())).thenReturn(queryMock)

                val expected = pokedex
                val actual = pokemonService.gameRegionalPokedex("hgss")

                actual shouldBe expected

                verify(queryMock).where(eq(Document("gen", 2)))
                verify(queryMock).where(eq(Filters.`in`("name", listOf("bulbasaur", "ivysaur", "venusaur"))))
                verify(gameServiceMock).gameFromKey(eq("hgss"))
                verify(cacheMock).pokedexOf(eq(goldAndSilver))
            }
        }
    }

    private fun <T> mockQuery(res: T, execute: Query.() -> T): Query {
        val query = mock<Query> { on { execute.invoke(this) } doReturn res }

        whenever(query.sort(any())).thenReturn(query)
        whenever(query.offset(any())).thenReturn(query)
        whenever(query.limit(any())).thenReturn(query)
        whenever(query.where(any())).thenReturn(query)

        return query
    }
}