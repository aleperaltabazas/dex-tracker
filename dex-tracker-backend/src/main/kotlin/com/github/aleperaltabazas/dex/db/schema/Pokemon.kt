package com.github.aleperaltabazas.dex.db.schema

import org.jetbrains.exposed.dao.id.LongIdTable

object PokemonTable : LongIdTable("pokemon") {
    val name = varchar("name", length = 50)
    val nationalDexNumber = integer("national_dex_number")
    val primaryAbility = varchar("primary_ability", length = 50)
    val secondaryAbility = varchar("secondary_ability", length = 50).nullable()
    val hiddenAbility = varchar("hidden_ability", length = 50).nullable()
    val gen = integer("gen")
}

object FormsTable : LongIdTable("forms") {
    val pokemonId = long("pokemon_id").references(PokemonTable.id)
    val name = varchar("name", length = 30)
}

object EvolutionsTable : LongIdTable("evolutions") {
    val name = varchar("name", length = 50)
    val pokemonId = long("pokemon_id").references(PokemonTable.id)
    val method = text("method")
}
