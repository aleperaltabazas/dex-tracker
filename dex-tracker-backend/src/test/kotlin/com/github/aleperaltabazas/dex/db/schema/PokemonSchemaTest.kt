package com.github.aleperaltabazas.dex.db.schema

import com.github.aleperaltabazas.dex.db.*
import com.github.aleperaltabazas.dex.db.extensions.evolutionMethodObjectMapper
import com.github.aleperaltabazas.dex.db.extensions.insert
import com.github.aleperaltabazas.dex.db.extensions.selectWhere
import com.github.aleperaltabazas.dex.db.fixture.*
import com.github.aleperaltabazas.dex.model.*
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.WordSpec
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class PokemonSchemaTest : WordSpec() {
    init {
        val db = Database.connect("jdbc:h2:mem:regular", "org.h2.Driver")

        "insert" should {
            "insert its evolutions and forms as well" {
                transaction(db) {
                    addLogger(StdOutSqlLogger)
                    SchemaUtils.create(*allTables.toTypedArray())

                    PokemonTable.selectAll().count() shouldBe 0L
                    EvolutionsTable.selectAll().count() shouldBe 0L
                    FormsTable.selectAll().count() shouldBe 0L

                    val id = PokemonTable.insert(pichu)[PokemonTable.id].value

                    PokemonTable.select { PokemonTable.id eq id }.count() shouldBe 1L
                    EvolutionsTable.select { EvolutionsTable.pokemonId eq id }.count() shouldBe 1L
                    FormsTable.select { FormsTable.pokemonId eq id }.count() shouldBe 1L
                }

            }
        }

        "selectWhere" should {
            "on a pokemon with no evolutions nor forms" {
                transaction(db) {
                    addLogger(StdOutSqlLogger)
                    SchemaUtils.create(*allTables.toTypedArray())
                    PokemonTable.insert { row ->
                        row[name] = "venusaur"
                        row[nationalDexNumber] = 3
                        row[primaryAbility] = "overgrow"
                        row[secondaryAbility] = null
                        row[hiddenAbility] = "chlorophyll"
                        row[gen] = 1
                    }

                    val expected = listOf(venusaur)

                    val actual = PokemonTable.selectWhere { PokemonTable.nationalDexNumber eq 3 }

                    actual.toList().size shouldBe 1
                    actual.toList() shouldBe expected
                }
            }

            "on a pokemon with one evolution but no forms" {
                transaction(db) {
                    addLogger(StdOutSqlLogger)
                    SchemaUtils.create(*allTables.toTypedArray())
                    val id = PokemonTable.insert { row ->
                        row[name] = "ivysaur"
                        row[nationalDexNumber] = 2
                        row[primaryAbility] = "overgrow"
                        row[secondaryAbility] = null
                        row[hiddenAbility] = "chlorophyll"
                        row[gen] = 1
                    } get PokemonTable.id

                    EvolutionsTable.insert { row ->
                        row[name] = "venusaur"
                        row[method] = evolutionMethodObjectMapper.writeValueAsString(LevelUp(level = 32))
                        row[pokemonId] = id.value
                    }

                    val expected = listOf(ivysaur)

                    val actual = PokemonTable.selectWhere { PokemonTable.nationalDexNumber eq 2 }

                    actual.toList().size shouldBe 1
                    actual.toList() shouldBe expected
                }
            }

            "on a pokemon with multiple evolutions but no forms" {
                transaction(db) {
                    addLogger(StdOutSqlLogger)
                    SchemaUtils.create(*allTables.toTypedArray())
                    val id = PokemonTable.insert { row ->
                        row[name] = "eevee"
                        row[nationalDexNumber] = 133
                        row[primaryAbility] = "run-away"
                        row[secondaryAbility] = "adaptability"
                        row[hiddenAbility] = "anticipation"
                        row[gen] = 1
                    } get PokemonTable.id

                    EvolutionsTable.insert {  row ->
                        row[name] = "vaporeon"
                        row[method] = evolutionMethodObjectMapper.writeValueAsString(UseItem(item = "water-stone"))
                        row[pokemonId] = id.value
                    }

                    EvolutionsTable.insert { row ->
                        row[name] = "jolteon"
                        row[method] = evolutionMethodObjectMapper.writeValueAsString(UseItem( item = "thunder-stone"))
                        row[pokemonId] = id.value
                    }

                    EvolutionsTable.insert { row ->
                        row[name] = "flareon"
                        row[method] = evolutionMethodObjectMapper.writeValueAsString(UseItem(item = "fire-stone"))
                        row[pokemonId] = id.value
                    }

                    val expected = listOf(eevee)

                    val actual = PokemonTable.selectWhere { PokemonTable.nationalDexNumber eq 133 }

                    actual.toList().size shouldBe 1
                    actual.toList() shouldBe expected
                }
            }

            "on a pokemon with an alternate form but no evolutions" {
                transaction(db) {
                    addLogger(StdOutSqlLogger)
                    SchemaUtils.create(*allTables.toTypedArray())
                    val id = PokemonTable.insert { row ->
                        row[name] = "giratina"
                        row[nationalDexNumber] = 487
                        row[primaryAbility] = "pressure"
                        row[secondaryAbility] = null
                        row[hiddenAbility] = null
                        row[gen] = 4
                    } get PokemonTable.id

                    FormsTable.insert { row ->
                        row[name] = "giratina-origin"
                        row[pokemonId] = id.value
                    }

                    val expected = listOf(giratina)

                    val actual = PokemonTable.selectWhere { PokemonTable.nationalDexNumber eq 487 }

                    actual.toList().size shouldBe 1
                    actual.toList() shouldBe expected
                }
            }

            "on a pokemon with multiple forms but no evolutions" {
                transaction(db) {
                    addLogger(StdOutSqlLogger)
                    SchemaUtils.create(*allTables.toTypedArray())
                    val id = PokemonTable.insert { row ->
                        row[name] = "unown"
                        row[nationalDexNumber] = 201
                        row[primaryAbility] = "levitate"
                        row[secondaryAbility] = null
                        row[hiddenAbility] = null
                        row[gen] = 2
                    } get PokemonTable.id

                    FormsTable.insert { row ->
                        row[name] = "unown-a"
                        row[pokemonId] = id.value
                    }

                    FormsTable.insert { row ->
                        row[name] = "unown-b"
                        row[pokemonId] = id.value
                    }

                    val expected = listOf(unown)

                    val actual = PokemonTable.selectWhere { PokemonTable.nationalDexNumber eq 201 }

                    actual.toList().size shouldBe 1
                    actual.toList() shouldBe expected
                }
            }

            "on a pokemon with an evolution and an alternate form" {
                transaction(db) {
                    addLogger(StdOutSqlLogger)
                    SchemaUtils.create(*allTables.toTypedArray())
                    val id = PokemonTable.insert { row ->
                        row[name] = "pichu"
                        row[nationalDexNumber] = 172
                        row[primaryAbility] = "static"
                        row[secondaryAbility] = null
                        row[hiddenAbility] = "lightning-rod"
                        row[gen] = 4
                    } get PokemonTable.id

                    EvolutionsTable.insert { row ->
                        row[name] = "pikachu"
                        row[method] = evolutionMethodObjectMapper.writeValueAsString(UseItem(item = "thunder-stone"))
                        row[pokemonId] = id.value
                    }

                    FormsTable.insert { row ->
                        row[name] = "spiky-eared-pichu"
                        row[pokemonId] = id.value
                    }

                    val expected = listOf(pichu)

                    val actual = PokemonTable.selectWhere { PokemonTable.nationalDexNumber eq 172 }

                    actual.toList().size shouldBe 1
                    actual.toList() shouldBe expected
                }
            }
        }
    }
}