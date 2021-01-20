package com.github.aleperaltabazas.dex.db.schema

import com.github.aleperaltabazas.dex.db.dao.DexPokemonDAO
import com.github.aleperaltabazas.dex.db.dao.PokedexDAO
import com.github.aleperaltabazas.dex.db.dao.UsersDAO
import com.github.aleperaltabazas.dex.db.extensions.*
import com.github.aleperaltabazas.dex.db.fixture.frank
import com.github.aleperaltabazas.dex.dto.dex.CaughtStatusDTO
import com.github.aleperaltabazas.dex.model.PokedexType
import com.github.aleperaltabazas.dex.model.UserDex
import com.github.aleperaltabazas.dex.model.UserDexPokemon
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.WordSpec
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class SessionSchemaTest : WordSpec() {
    init {
        val db = Database.connect("jdbc:h2:mem:regular", "org.h2.Driver")

        "findUser" should {
            "return the user associated to the token in the sessions table" {
                transaction(db) {
                    addLogger(StdOutSqlLogger)
                    SchemaUtils.create(UsersTable, PokedexTable, DexPokemonTable, SessionsTable)

//                    UsersTable.insert(frank)
//                    SessionsTable.insert { row ->
//                        row[userId] = 1L
//                        row[token] = "123"
//                    }


                    val user = UsersDAO.new {
                        username = "bulbasaur"
                    }

                    val dex = PokedexDAO.new {
                        region = "johto"
                        game = "hgss"
                        type = PokedexType.NATIONAL
                        userId = user.id.value
                    }

                    val poke = DexPokemonDAO.new {
                        name = "bulbasaur"
                        dexNumber = 1
                        caught = false
                        pokedexId = dex.id.value
                    }

                    val users = UsersDAO.all().with(UsersDAO::pokedex, PokedexDAO::pokemon)
                    println(users)

                    val dexs = PokedexDAO.all().with(PokedexDAO::pokemon)
                    println(dexs)

                    val pokes = DexPokemonDAO.all()
                    println(pokes.toList())

                    SessionsTable.findUser("123") shouldBe frank.copy(
                        id = 1,
                        pokedex = frank.pokedex.mapIndexed { idx, dex ->
                            dex.copy(
                                id = idx.toLong() + 1,
                            )
                        }
                    )
                }
            }
        }
//
//        "updateUserCaughtStatus" should {
//            "update as many rows as updated" {
//                transaction(db) {
//                    addLogger(StdOutSqlLogger)
//                    SchemaUtils.create(UsersTable, PokedexTable, DexPokemonTable, SessionsTable)
//                    UsersTable.insert(frank)
//                    SessionsTable.insert { row ->
//                        row[userId] = 1L
//                        row[token] = "123"
//                    }
//
//                    UsersTable.updateUserCaughtStatus(
//                        token = "123",
//                        status = listOf(
//                            CaughtStatusDTO(
//                                pokedexId = 1,
//                                dexNumber = 1,
//                                caught = false,
//                            ),
//                            CaughtStatusDTO(
//                                pokedexId = 1,
//                                dexNumber = 2,
//                                caught = true,
//                            ),
//                            CaughtStatusDTO(
//                                pokedexId = 1,
//                                dexNumber = 3,
//                                caught = false,
//                            ),
//                            CaughtStatusDTO(
//                                pokedexId = 2,
//                                dexNumber = 5,
//                                caught = false,
//                            ),
//                            CaughtStatusDTO(
//                                pokedexId = 3,
//                                dexNumber = 6,
//                                caught = true,
//                            )
//                        )
//                    ) shouldBe 5
//
//                    UsersTable.selectWhere { UsersTable.id eq 1 }.toUsers() shouldBe frank.copy(
//                        id = 1,
//                        pokedex = listOf(
//                            UserDex(
//                                game = "hgss",
//                                type = PokedexType.NATIONAL,
//                                region = "johto",
//                                pokemon = listOf(
//                                    UserDexPokemon(
//                                        name = "bulbasaur",
//                                        dexNumber = 1,
//                                        caught = false
//                                    ),
//                                    UserDexPokemon(
//                                        name = "ivysaur",
//                                        dexNumber = 2,
//                                        caught = true
//                                    ),
//                                    UserDexPokemon(
//                                        name = "venusaur",
//                                        dexNumber = 1,
//                                        caught = false
//                                    ),
//                                )
//                            ),
//                            UserDex(
//                                game = "hgss",
//                                type = PokedexType.NATIONAL,
//                                region = "johto",
//                                pokemon = listOf(
//                                    UserDexPokemon(
//                                        name = "charmeleon",
//                                        dexNumber = 5,
//                                        caught = false
//                                    ),
//                                )
//                            ),
//                            UserDex(
//                                game = "hgss",
//                                type = PokedexType.NATIONAL,
//                                region = "johto",
//                                pokemon = listOf(
//                                    UserDexPokemon(
//                                        name = "charizard",
//                                        dexNumber = 6,
//                                        caught = true
//                                    ),
//                                )
//                            )
//                        )
//                    )
//                }
//
//            }
//        }
    }
}