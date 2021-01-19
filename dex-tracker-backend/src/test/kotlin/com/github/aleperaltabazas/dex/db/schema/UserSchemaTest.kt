package com.github.aleperaltabazas.dex.db.schema

import com.github.aleperaltabazas.dex.db.extensions.insert
import com.github.aleperaltabazas.dex.db.extensions.selectWhere
import com.github.aleperaltabazas.dex.db.extensions.toUsers
import com.github.aleperaltabazas.dex.db.fixture.*
import com.github.aleperaltabazas.dex.model.User
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.WordSpec
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class UserSchemaTest : WordSpec() {
    init {
        val db = Database.connect("jdbc:h2:mem:regular", "org.h2.Driver")

        "insert" should {
            "add a row to the users table but no other table"{
                transaction(db) {
                    SchemaUtils.create(UsersTable, PokedexTable, DexPokemonTable)
                    UsersTable.selectAll().count() shouldBe 0L
                    PokedexTable.selectAll().count() shouldBe 0L
                    DexPokemonTable.selectAll().count() shouldBe 0L

                    UsersTable.insert(richard)

                    UsersTable.selectAll().count() shouldBe 1L
                    PokedexTable.selectAll().count() shouldBe 0L
                    DexPokemonTable.selectAll().count() shouldBe 0L
                }

            }
        }

        "toUsers" should {
            "return an empty list" {
                transaction(db) {
                    addLogger(StdOutSqlLogger)
                    SchemaUtils.create(UsersTable, PokedexTable, DexPokemonTable)

                    UsersTable.selectWhere { Op.TRUE }.toUsers() shouldBe emptyList<User>()
                }
            }

            "return a list of just one user with no pokedex" {
                transaction(db) {
                    addLogger(StdOutSqlLogger)
                    SchemaUtils.create(UsersTable, PokedexTable, DexPokemonTable)

                    UsersTable.insert(andrew)

                    UsersTable.selectWhere { Op.TRUE }.toUsers() shouldBe listOf(andrew.copy(id = 1))
                }
            }

            "return a list of two users with no pokedex" {
                transaction(db) {
                    addLogger(StdOutSqlLogger)
                    SchemaUtils.create(UsersTable, PokedexTable, DexPokemonTable)

                    UsersTable.insert(andrew, brenda)

                    UsersTable.selectWhere { Op.TRUE }.toUsers() shouldBe listOf(
                        andrew.copy(id = 1),
                        brenda.copy(id = 2)
                    )
                }
            }

            "return a list of one user with an empty pokedex" {
                transaction(db) {
                    addLogger(StdOutSqlLogger)
                    SchemaUtils.create(UsersTable, PokedexTable, DexPokemonTable)

                    UsersTable.insert(daniel)

                    UsersTable.selectWhere { Op.TRUE }.toUsers() shouldBe listOf(
                        daniel.copy(id = 1)
                    )
                }
            }

            "return a list of two users with a pokedex for the same game" {
                transaction(db) {
                    addLogger(StdOutSqlLogger)
                    SchemaUtils.create(UsersTable, PokedexTable, DexPokemonTable)

                    UsersTable.insert(daniel, emily)

                    UsersTable.selectWhere { Op.TRUE }.toUsers() shouldBe listOf(
                        daniel.copy(id = 1),
                        emily.copy(id = 2)
                    )
                }
            }
        }
    }
}