package com.github.aleperaltabazas.dex.db

import com.github.aleperaltabazas.dex.db.schema.*
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder

typealias Where = SqlExpressionBuilder.() -> Op<Boolean>

val allTables = listOf(
    PokemonTable,
    FormsTable,
    EvolutionsTable,
    UsersTable,
    PokedexTable,
    DexPokemonTable,
)
