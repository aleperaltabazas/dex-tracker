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
    val evolutions by EvolutionDAO referrersOn EvolutionsTable.pokemonId
    val forms by FormDAO referrersOn FormsTable.pokemonId
    var gen by PokemonTable.gen
}

class FormDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<FormDAO>(FormsTable)

    var name by FormsTable.name
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
