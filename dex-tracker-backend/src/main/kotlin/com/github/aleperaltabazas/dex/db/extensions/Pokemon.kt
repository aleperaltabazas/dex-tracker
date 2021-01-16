package com.github.aleperaltabazas.dex.db.extensions

import com.github.aleperaltabazas.dex.db.Where
import com.github.aleperaltabazas.dex.db.schema.EvolutionsTable
import com.github.aleperaltabazas.dex.db.schema.FormsTable
import com.github.aleperaltabazas.dex.db.schema.PokemonTable
import com.github.aleperaltabazas.dex.model.Pokemon
import org.jetbrains.exposed.sql.select

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
    .asSequence()
    .map { (pokemon, formsAndEvos) ->
        pokemon.copy(
            evolutions = formsAndEvos.filter { row -> row.getOrNull(EvolutionsTable.id) != null }
                .map { it.toEvolution() },
            forms = formsAndEvos.filter { row -> row.getOrNull(FormsTable.id) != null }
                .map { it.toForm() }
        )
    }


