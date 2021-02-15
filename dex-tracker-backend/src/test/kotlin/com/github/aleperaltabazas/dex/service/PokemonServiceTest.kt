package com.github.aleperaltabazas.dex.service

import arrow.core.left
import arrow.core.right
import com.github.aleperaltabazas.dex.cache.pokedex.RegionalPokedexCache
import com.github.aleperaltabazas.dex.db.fixture.bulbasaur
import com.github.aleperaltabazas.dex.db.fixture.ivysaur
import com.github.aleperaltabazas.dex.db.fixture.venusaur
import com.github.aleperaltabazas.dex.dto.dex.DexEntryDTO
import com.github.aleperaltabazas.dex.dto.dex.GameDTO
import com.github.aleperaltabazas.dex.dto.dex.GamePokedexDTO
import com.github.aleperaltabazas.dex.exception.NotFoundException
import com.github.aleperaltabazas.dex.mock.createQueryMock
import com.github.aleperaltabazas.dex.model.Game
import com.github.aleperaltabazas.dex.model.GamePokedex
import com.github.aleperaltabazas.dex.model.PokedexType
import com.github.aleperaltabazas.dex.storage.Collection
import com.github.aleperaltabazas.dex.storage.Query
import com.github.aleperaltabazas.dex.storage.Storage
import com.github.aleperaltabazas.dex.utils.IdGenerator
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import com.nhaarman.mockito_kotlin.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.core.test.TestCase
import io.kotest.matchers.shouldBe
import org.bson.Document

class PokedexServiceTest : WordSpec() {
    private val cacheMock: RegionalPokedexCache = mock {}
    private val storageMock: Storage = mock {}
    private val gameServiceMock: GameService = mock {}
    private val idGenerator: IdGenerator = mock {}
    private val pokedexService = PokedexService(
        regionalPokedexCache = cacheMock,
        storage = storageMock,
        gameService = gameServiceMock,
        idGenerator = idGenerator,
    )

    override fun beforeEach(testCase: TestCase) {
        reset(cacheMock, storageMock, gameServiceMock)
    }

    init {
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
                val queryMock: Query = createQueryMock(ivysaur, Query::findOne)

                whenever(cacheMock.pokedexOf(any())).thenReturn(goldAndSilverPokedex)
                whenever(gameServiceMock.gameFromKey(any())).thenReturn(goldAndSilver)
                whenever(storageMock.query(any())).thenReturn(queryMock)

                val expected = ivysaur
                val actual = pokedexService.pokemon("gs", 2.left())

                actual shouldBe expected

                verify(queryMock).where(eq(Document("gen", goldAndSilver.gen).append("national_pokedex_number", 2)))
                verify(queryMock).limit(eq(1))
                verify(storageMock).query(eq(Collection.POKEMON))
                verify(gameServiceMock).gameFromKey(eq("gs"))
                verify(cacheMock).pokedexOf(eq(goldAndSilver))
            }

            "find a pokemon by its name" {
                val queryMock = createQueryMock(ivysaur, Query::findOne)

                whenever(queryMock.where(any())).thenReturn(queryMock)
                whenever(queryMock.limit(any())).thenReturn(queryMock)
                whenever(cacheMock.pokedexOf(any())).thenReturn(goldAndSilverPokedex)
                whenever(gameServiceMock.gameFromKey(any())).thenReturn(goldAndSilver)
                whenever(storageMock.query(any())).thenReturn(queryMock)

                val expected = ivysaur
                val actual = pokedexService.pokemon("gs", "ivysaur".right())

                actual shouldBe expected

                verify(queryMock).where(eq(Document("gen", goldAndSilver.gen).append("name", "ivysaur")))
                verify(queryMock).limit(eq(1))
                verify(storageMock).query(eq(Collection.POKEMON))

                verify(gameServiceMock).gameFromKey(eq("gs"))
                verify(cacheMock).pokedexOf(eq(goldAndSilver))
            }

            "throw a NotFoundException if no pokemon matches either criteria" {
                val queryMock: Query = createQueryMock(null, Query::findOne)
                whenever(cacheMock.pokedexOf(any())).thenReturn(goldAndSilverPokedex)
                whenever(gameServiceMock.gameFromKey(any())).thenReturn(goldAndSilver)
                whenever(storageMock.query(any())).thenReturn(queryMock)

                shouldThrow<NotFoundException> {
                    pokedexService.pokemon("gs", "ivysaur".right())
                }

                verify(queryMock).where(eq(Document("gen", goldAndSilver.gen).append("name", "ivysaur")))

                shouldThrow<NotFoundException> {
                    pokedexService.pokemon("gs", 2.left())
                }

                verify(queryMock).where(eq(Document("gen", goldAndSilver.gen).append("national_pokedex_number", 2)))
            }
        }

        "gameNationalPokedex" should {
            val pokedex = GamePokedex(
                type = PokedexType.NATIONAL,
                game = Game(
                    title = "gs",
                    fullTitle = "Gold and Silver",
                    spritePokemon = "ho-oh",
                    region = "johto",
                    pokeapiId = "123",
                    gen = 2,
                ),
                pokemon = listOf(
                    "bulbasaur",
                    "ivysaur",
                    "venusaur",
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
                val queryMock = createQueryMock(listOf(bulbasaur, ivysaur, venusaur), Query::findAll)

                whenever(gameServiceMock.gameFromKey(any())).thenReturn(goldAndSilver)
                whenever(storageMock.query(any())).thenReturn(queryMock)

                val expected = pokedex
                val actual = pokedexService.gameNationalPokedex("hgss")

                actual shouldBe expected

                verify(queryMock).where(eq(Document("gen", 2)))
                verify(queryMock).sort(eq(Sorts.ascending("national_pokedex_number")))
                verify(gameServiceMock).gameFromKey(eq("hgss"))
            }
        }

        "gameRegionalPokedex" should {
            val pokedex = GamePokedex(
                type = PokedexType.REGIONAL,
                game = Game(
                    title = "gs",
                    fullTitle = "Gold and Silver",
                    spritePokemon = "ho-oh",
                    region = "johto",
                    pokeapiId = "123",
                    gen = 2,
                ),
                pokemon = listOf(
                    "bulbasaur",
                    "ivysaur",
                    "venusaur",
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
                val queryMock = createQueryMock(listOf(ivysaur, bulbasaur, venusaur), Query::findAll)

                whenever(cacheMock.pokedexOf(any())).thenReturn(goldAndSilverPokedex)
                whenever(gameServiceMock.gameFromKey(any())).thenReturn(goldAndSilver)
                whenever(storageMock.query(any())).thenReturn(queryMock)

                val expected = pokedex
                val actual = pokedexService.gameRegionalPokedex("hgss")

                actual shouldBe expected

                verify(queryMock).where(eq(Document("gen", 2)))
                verify(queryMock).where(eq(Filters.`in`("name", listOf("bulbasaur", "ivysaur", "venusaur"))))
                verify(gameServiceMock).gameFromKey(eq("hgss"))
                verify(cacheMock).pokedexOf(eq(goldAndSilver))
            }
        }
    }
}