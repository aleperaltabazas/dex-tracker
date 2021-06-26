package com.github.aleperaltabazas.dex.model

import com.github.aleperaltabazas.dex.dto.dex.DexUpdateDTO
import com.github.aleperaltabazas.dex.dto.dex.UpdateUserDTO
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe

class UserTest : WordSpec() {
    init {
        val user = User(
            userId = "123",
            pokedex = emptyList(),
            mail = "test@test.com",
        )

        val dex = UserDex(
            userDexId = "123",
            game = Game(
                name = "rby-national",
                displayName = "Red, Blue and Yellow - National Pokedex",
                gen = 1,
            ),
            type = PokedexType.NATIONAL,
            region = "kanto",
            pokemon = listOf(
                UserDexPokemon(
                    name = "bulbasaur",
                    dexNumber = 1,
                    caught = false,
                )
            ),
        )

        "update" should {
            "change the user's username" {
                user.update(UpdateUserDTO(username = "foo")) shouldBe user.copy(username = "foo")
                user.copy(username = "foo").update(UpdateUserDTO(username = "bar")) shouldBe user.copy(username = "bar")
            }

            "keep the current username if there's no username to change" {
                user.update(UpdateUserDTO()) shouldBe user
                user.copy(username = "foo").update(UpdateUserDTO()) shouldBe user.copy(username = "foo")
            }

            "keep the current dex with no changes if there is no change with matching dex id" {
                user.copy(
                    pokedex = listOf(
                        dex,
                        dex.copy(userDexId = "456"),
                    ),
                ).update(changes = UpdateUserDTO()) shouldBe user.copy(
                    pokedex = listOf(
                        dex,
                        dex.copy(userDexId = "456"),
                    )
                )
            }

            "update only a dex if there's a change with its key" {
                val expected = user.copy(
                    pokedex = listOf(
                        dex.copy(name = "foo"),
                        dex.copy(userDexId = "456"),
                    ),
                )

                val actual = user.copy(
                    pokedex = listOf(
                        dex,
                        dex.copy(userDexId = "456"),
                    ),
                ).update(
                    UpdateUserDTO(
                        dex = mapOf(
                            "123" to DexUpdateDTO(
                                name = "foo",
                                caught = emptyList(),
                            )
                        )
                    )
                )

                actual shouldBe expected
            }
        }
    }
}
