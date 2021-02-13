package com.github.aleperaltabazas.dex.model

import com.github.aleperaltabazas.dex.db.fixture.brenda
import com.github.aleperaltabazas.dex.db.fixture.daniel
import com.github.aleperaltabazas.dex.db.fixture.frank
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe

class UserTest : WordSpec() {
    init {
        "mergePokedex" should {
            "return the same user" {
                brenda.mergePokedex(emptyList()) shouldBe brenda
                daniel.mergePokedex(emptyList()) shouldBe daniel
            }

            "return a new user with a new pokedex" {
                val dex = UserDex(
                    userDexId = "123",
                    game = "hgss",
                    type = PokedexType.NATIONAL,
                    region = "johto",
                    pokemon = emptyList()
                )

                brenda.mergePokedex(listOf(dex)) shouldBe brenda.copy(pokedex = listOf(dex))
                daniel.mergePokedex(listOf(dex)) shouldBe daniel.copy(pokedex = daniel.pokedex + dex)
            }

            "replace a pokedex if its id matches with any of the new ones" {
                val dex = UserDex(
                    userDexId = "UD-3",
                    game = "hgss",
                    type = PokedexType.NATIONAL,
                    region = "johto",
                    pokemon = listOf(
                        UserDexPokemon(
                            name = "bulbasaur",
                            dexNumber = 1,
                            caught = true,
                        ),
                        UserDexPokemon(
                            name = "ivysaur",
                            dexNumber = 2,
                            caught = true,
                        ),
                        UserDexPokemon(
                            name = "venusaur",
                            dexNumber = 1,
                            caught = true,
                        ),
                    )
                )

                frank.mergePokedex(listOf(dex)) shouldBe frank.copy(
                    pokedex = listOf(dex) + frank.pokedex.drop(1)
                )
            }
        }
    }
}