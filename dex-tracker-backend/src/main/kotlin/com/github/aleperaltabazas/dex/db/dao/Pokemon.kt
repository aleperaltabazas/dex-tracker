package com.github.aleperaltabazas.dex.db.dao

import com.github.aleperaltabazas.dex.db.schema.EvolutionsTable
import com.github.aleperaltabazas.dex.db.schema.FormsTable
import com.github.aleperaltabazas.dex.db.schema.PokemonTable
import com.github.aleperaltabazas.dex.db.schema.StatsTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PokemonDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<PokemonDAO>(PokemonTable)

    val name by PokemonTable.name
    val nationalPokedexNumber by PokemonTable.nationalDexNumber
    val primaryAbility by PokemonTable.primaryAbility
    val secondaryAbility by PokemonTable.secondaryAbility
    val hiddenAbility by PokemonTable.hiddenAbility
    val primaryType by PokemonTable.primaryType
    val secondaryType by PokemonTable.secondaryType
    val maleProbability by PokemonTable.maleProbability
    val femaleProbability by PokemonTable.femaleProbability
    val hp by PokemonTable.hp
    val attack by PokemonTable.attack
    val defense by PokemonTable.defense
    val specialAttack by PokemonTable.specialAttack
    val specialDefense by PokemonTable.specialDefense
    val speed by PokemonTable.speed
    val evolutions by EvolutionDAO referrersOn EvolutionsTable.pokemonId
    val forms by FormDAO referrersOn FormsTable.pokemonId
}

class StatsDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<StatsDAO>(StatsTable)

    val hp by StatsTable.hp
    val attack by StatsTable.attack
    val defense by StatsTable.defense
    val specialAttack by StatsTable.specialAttack
    val specialDefense by StatsTable.specialDefense
    val speed by StatsTable.speed
}

class FormDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<FormDAO>(FormsTable)

    val name by FormsTable.name
    val stats by StatsDAO optionalReferencedOn FormsTable.statsId
    val primaryType by FormsTable.primaryType
    val secondaryType by FormsTable.secondaryType
}

class EvolutionDAO(
    id: EntityID<Long>,
) : LongEntity(id) {
    companion object : LongEntityClass<EvolutionDAO>(EvolutionsTable)

    val name by EvolutionsTable.name
    val method by EvolutionsTable.method
}
