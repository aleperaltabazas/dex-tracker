package com.github.aleperaltabazas.dex.db.schema

import org.jetbrains.exposed.dao.id.LongIdTable

object PokemonTable : LongIdTable("pokemon") {
    val name = varchar("name", length = 50).uniqueIndex()
    val nationalDexNumber = integer("national_dex_number").uniqueIndex()
    val primaryAbility = varchar("primary_ability", length = 50)
    val secondaryAbility = varchar("secondary_ability", length = 50).nullable()
    val hiddenAbility = varchar("hidden_ability", length = 50).nullable()
    val primaryType = varchar("primary_type", length = 8)
    val secondaryType = varchar("secondary_type", length = 8).nullable()
    val maleProbability = double("male_prob").nullable()
    val femaleProbability = double("female_prob").nullable()
    val hp = integer("hp")
    val attack = integer("attack")
    val defense = integer("defense")
    val specialAttack = integer("special_attack")
    val specialDefense = integer("special_defense")
    val speed = integer("speed")
    val gen = integer("gen")
}

object FormsTable : LongIdTable("forms") {
    val pokemonId = long("pokemon_id").references(PokemonTable.id)
    val name = varchar("name", length = 30)
    val hp = integer("hp")
    val attack = integer("attack")
    val defense = integer("defense")
    val specialAttack = integer("special_attack")
    val specialDefense = integer("special_defense")
    val speed = integer("speed")
    val primaryType = varchar("primary_type", length = 20)
    val secondaryType = varchar("secondary_type", length = 20).nullable()
}

object EvolutionsTable : LongIdTable("evolutions") {
    val name = varchar("name", length = 50)
    val pokemonId = long("pokemon_id").references(PokemonTable.id)
    val method = text("method")
}
