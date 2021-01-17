package com.github.aleperaltabazas.dex.db.extensions

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.aleperaltabazas.dex.db.schema.EvolutionsTable
import com.github.aleperaltabazas.dex.model.Evolution
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.batchInsert

val evolutionMethodObjectMapper: ObjectMapper = jacksonObjectMapper()
    .registerModule(KotlinModule())
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
    .apply { this.propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE }

fun ResultRow.toEvolution(): Evolution = Evolution(
    name = this[EvolutionsTable.name],
    method = evolutionMethodObjectMapper.readValue(this[EvolutionsTable.method])
)

fun EvolutionsTable.insert(evolutions: List<Evolution>, pokemonId: Long) = batchInsert(evolutions) { e ->
    this[name] = e.name
    this[method] = evolutionMethodObjectMapper.writeValueAsString(e.method)
    this[EvolutionsTable.pokemonId] = pokemonId
}
