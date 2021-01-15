package com.github.aleperaltabazas.dex.db.extensions

import com.github.aleperaltabazas.dex.db.Where
import com.github.aleperaltabazas.dex.db.schema.EvolutionsTable
import com.github.aleperaltabazas.dex.db.schema.FormsTable
import com.github.aleperaltabazas.dex.db.schema.PokemonTable
import com.github.aleperaltabazas.dex.model.Pokemon
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement

fun PokemonTable.insert(
    pokemon: Pokemon
): InsertStatement<Number> {
    val insert = insert {
        it[name] = pokemon.name
        it[nationalDexNumber] = pokemon.nationalPokedexNumber
        it[gen] = pokemon.gen
        it[primaryAbility] = pokemon.primaryAbility
        it[secondaryAbility] = pokemon.secondaryAbility
        it[hiddenAbility] = pokemon.hiddenAbility
    }

    val id = insert[id]

    pokemon.evolutions.forEach { e -> EvolutionsTable.insert(e, id.value) }
    pokemon.forms.forEach { f -> FormsTable.insert(f, id.value) }

    return insert
}

fun PokemonTable.selectWhere(where: Where) = this
    .leftJoin(EvolutionsTable)
    .leftJoin(FormsTable)
    .select(where)
    .orderBy(nationalDexNumber)
    .groupBy { row ->
        Pokemon(
            name = row[name],
            gen = row[gen],
            nationalPokedexNumber = row[nationalDexNumber],
            primaryAbility = row[primaryAbility],
            secondaryAbility = row[secondaryAbility],
            hiddenAbility = row[hiddenAbility],
            forms = emptyList(),
            evolutions = emptyList(),
        )
    }
    .map { (pokemon, formsAndEvos) ->
        pokemon.copy(
            evolutions = formsAndEvos.filter { row -> row.getOrNull(EvolutionsTable.id) != null }
                .map { it.toEvolution() },
            forms = formsAndEvos.filter { row -> row.getOrNull(FormsTable.id) != null }
                .map { it.toForm() }
        )
    }


