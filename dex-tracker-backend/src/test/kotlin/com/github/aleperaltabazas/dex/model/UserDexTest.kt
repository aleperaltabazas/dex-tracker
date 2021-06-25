package com.github.aleperaltabazas.dex.model

import com.github.aleperaltabazas.dex.dto.dex.DexUpdateDTO
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe

class UserDexTest : WordSpec() {
    init {
        val dex = UserDex(
            userDexId = "123",
            game = Game(
                name = "rby-national",
                displayName = "Red, Blue and Yellow - National Pokedex",
                gen = 1,
            ),
            type = PokedexType.NATIONAL,
            region = "kanto",
            pokemon = emptyList(),
        )

        "update" should {
            "update the dex name" {
                dex.update(
                    DexUpdateDTO(
                        name = "foo",
                        caught = emptyList(),
                        favourites = emptyList(),
                    )
                ) shouldBe dex.copy(name = "foo")
                dex.copy(name = "foo").update(
                    DexUpdateDTO(
                        name = "bar",
                        caught = emptyList(),
                        favourites = emptyList(),
                    )
                ) shouldBe dex.copy(
                    name = "bar",
                )
            }

            "keep the current name if the update is null" {
                dex.update(DexUpdateDTO(caught = emptyList(), favourites = emptyList())) shouldBe dex
                dex.copy(name = "foo").update(
                    DexUpdateDTO(
                        caught = emptyList(),
                        favourites = emptyList(),
                    )
                ) shouldBe dex.copy(name = "foo")
            }

            val bulbasaur = { caught: Boolean ->
                UserDexPokemon(
                    name = "bulbasaur",
                    dexNumber = 1,
                    caught = caught,
                )
            }
            val ivysaur = { caught: Boolean ->
                UserDexPokemon(
                    name = "ivysaur",
                    dexNumber = 2,
                    caught = caught,
                )
            }
            val venusaur = { caught: Boolean ->
                UserDexPokemon(
                    name = "venusaur",
                    dexNumber = 3,
                    caught = caught,
                )
            }

            "mark as caught those pokemon whose number is in the update" {
                val expected = dex.copy(
                    pokemon = listOf(
                        bulbasaur(false),
                        ivysaur(true),
                        venusaur(true)
                    ),
                    caught = 2,
                )
                val actual = dex.copy(pokemon = listOf(bulbasaur(false), ivysaur(false), venusaur(false)))
                    .update(DexUpdateDTO(caught = listOf(2, 3), favourites = emptyList()))

                actual shouldBe expected
            }

            "mark as uncaught those pokemon whose number is not in the update" {
                val expected = dex.copy(
                    pokemon = listOf(
                        bulbasaur(false),
                        ivysaur(true),
                        venusaur(false),
                    ),
                    caught = 1,
                )

                val actual = dex.copy(pokemon = listOf(bulbasaur(true), ivysaur(false), venusaur(false)))
                    .update(DexUpdateDTO(caught = listOf(2), favourites = emptyList()))

                actual shouldBe expected
            }
        }
    }
}
