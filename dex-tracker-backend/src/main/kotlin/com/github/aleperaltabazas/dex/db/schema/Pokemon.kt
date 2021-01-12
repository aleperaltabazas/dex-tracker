package com.github.aleperaltabazas.dex.db.schema

import com.github.aleperaltabazas.dex.db.GenericTable
import com.github.aleperaltabazas.dex.db.model.Evolution
import com.github.aleperaltabazas.dex.db.model.Form
import com.github.aleperaltabazas.dex.db.model.Pokemon
import com.github.aleperaltabazas.dex.db.model.Stats

object Pokemons : GenericTable<Pokemon>("pokemons") {
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
}

object Forms : GenericTable<Form>("forms") {
    val name = varchar("name", length = 30)
    val pokemonId = long("pokemon_id") references Pokemons.id
    val statsId = (long("stats_id") references Statses.id).nullable()
}

object Statses : GenericTable<Stats>("stats") {
    val hp = integer("hp")
    val attack = integer("attack")
    val defense = integer("defense")
    val specialAttack = integer("special_attack")
    val specialDefense = integer("special_defense")
    val speed = integer("speed")
}

object Evolutions : GenericTable<Evolution>("evolutions") {
    val name = varchar("name", length = 50)
    val pokemonId = long("pokemon_id") references Pokemons.id
    val method = varchar("method", length = 200)
}
