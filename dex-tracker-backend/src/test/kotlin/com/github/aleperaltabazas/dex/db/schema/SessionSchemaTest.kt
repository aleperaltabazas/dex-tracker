package com.github.aleperaltabazas.dex.db.schema

import com.github.aleperaltabazas.dex.db.extensions.insert
import com.github.aleperaltabazas.dex.db.extensions.toUsers
import com.github.aleperaltabazas.dex.db.extensions.updateCaught
import com.github.aleperaltabazas.dex.db.fixture.emily
import com.github.aleperaltabazas.dex.db.fixture.frank
import com.github.aleperaltabazas.dex.model.PokedexType
import com.github.aleperaltabazas.dex.model.UserDex
import com.github.aleperaltabazas.dex.model.UserDexPokemon
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.WordSpec
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class SessionSchemaTest : WordSpec() {
    init {
        val db = Database.connect("jdbc:h2:mem:regular", "org.h2.Driver")

        "a" {
            "b"{
                transaction(db) {
                    UsersTable.insert(emily, frank)

                    DexPokemonTable.updateCaught(
                        caught = true,
                        dexNumber = 1,
                        pokedexId = 1L,
                    ) shouldBe 1

                    UsersTable.selectAll().toUsers() shouldBe listOf(
                        emily.copy(
                            id = 1,
                            pokedex = listOf(
                                UserDex(
                                    id = 1,
                                    game = "hgss",
                                    type = PokedexType.NATIONAL,
                                    region = "johto",
                                    pokemon = listOf(
                                        UserDexPokemon(
                                            name = "bulbasaur",
                                            dexNumber = 1,
                                            caught = true
                                        )
                                    )
                                )
                            )
                        ),
                        frank.copy(id = 2)
                    )
                }
            }
        }
    }
}