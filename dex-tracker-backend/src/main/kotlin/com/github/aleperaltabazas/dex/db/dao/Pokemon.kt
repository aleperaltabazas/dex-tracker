package com.github.aleperaltabazas.dex.db.dao

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.aleperaltabazas.dex.db.schema.EvolutionsTable
import com.github.aleperaltabazas.dex.db.schema.FormsTable
import com.github.aleperaltabazas.dex.db.schema.PokemonTable
import com.github.aleperaltabazas.dex.model.EvolutionMethod
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PokemonDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<PokemonDAO>(PokemonTable)

    var name by PokemonTable.name
    var nationalPokedexNumber by PokemonTable.nationalDexNumber
    var primaryAbility by PokemonTable.primaryAbility
    var secondaryAbility by PokemonTable.secondaryAbility
    var hiddenAbility by PokemonTable.hiddenAbility
    var primaryType by PokemonTable.primaryType
    var secondaryType by PokemonTable.secondaryType
    var maleProbability by PokemonTable.maleProbability
    var femaleProbability by PokemonTable.femaleProbability
    var hp by PokemonTable.hp
    var attack by PokemonTable.attack
    var defense by PokemonTable.defense
    var specialAttack by PokemonTable.specialAttack
    var specialDefense by PokemonTable.specialDefense
    var speed by PokemonTable.speed
    var gen by PokemonTable.gen
    val evolutions by EvolutionDAO referrersOn EvolutionsTable.pokemonId
    val forms by FormDAO referrersOn FormsTable.pokemonId
}

class FormDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<FormDAO>(FormsTable)

    var name by FormsTable.name
    var hp by FormsTable.hp
    var attack by FormsTable.attack
    var defense by FormsTable.defense
    var specialAttack by FormsTable.specialAttack
    var specialDefense by FormsTable.specialDefense
    var speed by FormsTable.speed
    var primaryType by FormsTable.primaryType
    var secondaryType by FormsTable.secondaryType
}

class EvolutionDAO(
    private val objectMapper: ObjectMapper,
    id: EntityID<Long>,
) : LongEntity(id) {
    companion object : LongEntityClass<EvolutionDAO>(EvolutionsTable)

    var name by EvolutionsTable.name
    var method: EvolutionMethod by EvolutionsTable.method.transform(
        toColumn = { objectMapper.writeValueAsString(it) },
        toReal = { objectMapper.readValue(it) }
    )
}
