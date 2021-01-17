package com.github.aleperaltabazas.dex.integration

import com.github.aleperaltabazas.dex.cache.pokedex.GamePokedexCache
import com.github.aleperaltabazas.dex.config.TestContext
import com.github.aleperaltabazas.dex.connector.RestConnector
import com.github.aleperaltabazas.dex.db.allTables
import com.github.aleperaltabazas.dex.db.fixture.eevee
import com.github.aleperaltabazas.dex.db.fixture.ivysaur
import com.github.aleperaltabazas.dex.db.fixture.loadFixture
import com.github.aleperaltabazas.dex.db.fixture.venusaur
import com.github.aleperaltabazas.dex.dto.dex.DexEntryDTO
import com.github.aleperaltabazas.dex.dto.dex.FormDTO
import com.github.aleperaltabazas.dex.dto.dex.GameDTO
import com.github.aleperaltabazas.dex.dto.dex.GamePokedexDTO
import com.github.aleperaltabazas.dex.mock.PokeapiConnector
import com.github.aleperaltabazas.dex.model.PokedexType
import com.github.aleperaltabazas.dex.service.PokemonService
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.WordSpec
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class PokemonServiceTest : WordSpec() {
    init {
        val db: Database = TestContext.get("db")
        val pokeapiMock: RestConnector = TestContext.get("pokeapiConnector")
        val pokemonService: PokemonService = TestContext.get("pokemonService")
        val gamePokedexCache: GamePokedexCache = TestContext.get("gamePokedexCache")

        "gameNationalPokedex" should {
            "return ivysaur, venusaur and eevee for game key rby" {
                transaction(db) {
                    SchemaUtils.create(*allTables.toTypedArray())
                    loadFixture()

                    PokeapiConnector.kantoPokedex(pokeapiMock)
                    gamePokedexCache.start()

                    val expected = GamePokedexDTO(
                        game = GameDTO(
                            title = "rby",
                            fullTitle = "Red, Blue and Yellow",
                            spritePokemon = "pikachu"
                        ),
                        region = "kanto",
                        type = PokedexType.NATIONAL,
                        pokemon = listOf(
                            ivysaur,
                            venusaur,
                            eevee,
                        ).map { DexEntryDTO(it.name, it.nationalPokedexNumber, it.forms.map { f -> FormDTO(f) }) }
                    )

                    val actual = pokemonService.gameNationalPokedex("rby")

                    actual shouldBe expected
                }

            }
        }
    }
}