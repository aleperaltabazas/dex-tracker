package com.github.aleperaltabazas.dex.db.extensions

import com.github.aleperaltabazas.dex.db.Where
import com.github.aleperaltabazas.dex.db.schema.EvolutionsTable
import com.github.aleperaltabazas.dex.db.schema.FormsTable
import com.github.aleperaltabazas.dex.db.schema.PokemonTable
import com.github.aleperaltabazas.dex.model.Pokemon
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

fun PokemonTable.insert(pokemon: Pokemon) = insert { row ->
    row[name] = pokemon.name
    row[nationalDexNumber] = pokemon.nationalPokedexNumber
    row[primaryAbility] = pokemon.primaryAbility
    row[secondaryAbility] = pokemon.secondaryAbility
    row[hiddenAbility] = pokemon.hiddenAbility
    row[gen] = pokemon.gen
}
    .also {
        EvolutionsTable.insert(pokemon.evolutions, it[id].value)
        FormsTable.insert(pokemon.forms, it[id].value)
    }

fun PokemonTable.selectWhere(
    where: Where,
    limit: Int? = null
) = this
    .leftJoin(EvolutionsTable)
    .leftJoin(FormsTable)
    .select(where)
    .let { limit?.let { l -> it.limit(l) } ?: it }
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
    .asSequence()
    .map { (pokemon, formsAndEvos) ->
        pokemon.copy(
            evolutions = formsAndEvos.filter { row -> row.getOrNull(EvolutionsTable.id) != null }
                .map { it.toEvolution() },
            forms = formsAndEvos.filter { row -> row.getOrNull(FormsTable.id) != null }
                .map { it.toForm() }
        )
    }


