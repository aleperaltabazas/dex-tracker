package com.github.aleperaltabazas.dex.db.fixture

import com.github.aleperaltabazas.dex.db.extensions.insert
import com.github.aleperaltabazas.dex.db.schema.PokemonTable
import com.github.aleperaltabazas.dex.model.*

val ivysaur = Pokemon(
    name = "ivysaur",
    nationalPokedexNumber = 2,
    primaryAbility = "overgrow",
    secondaryAbility = null,
    hiddenAbility = "chlorophyll",
    evolutions = listOf(
        Evolution(
            name = "venusaur",
            method = LevelUp(level = 32)
        ),
    ),
    forms = emptyList(),
    gen = 1,
)

val venusaur = Pokemon(
    name = "venusaur",
    nationalPokedexNumber = 3,
    primaryAbility = "overgrow",
    secondaryAbility = null,
    hiddenAbility = "chlorophyll",
    gen = 1,
    forms = emptyList(),
    evolutions = emptyList(),
)

val eevee = Pokemon(
    name = "eevee",
    nationalPokedexNumber = 133,
    primaryAbility = "run-away",
    secondaryAbility = "adaptability",
    hiddenAbility = "anticipation",
    evolutions = listOf(
        Evolution(
            name = "vaporeon",
            method = UseItem(item = "water-stone"),
        ),
        Evolution(
            name = "jolteon",
            method = UseItem(item = "thunder-stone"),
        ),
        Evolution(
            name = "flareon",
            method = UseItem(item = "fire-stone"),
        ),
    ),
    forms = emptyList(),
    gen = 1,
)

val giratina = Pokemon(
    name = "giratina",
    nationalPokedexNumber = 487,
    primaryAbility = "pressure",
    secondaryAbility = null,
    hiddenAbility = null,
    gen = 4,
    forms = listOf(
        Form(
            name = "giratina-origin"
        ),
    ),
    evolutions = emptyList(),
)

val unown = Pokemon(
    name = "unown",
    nationalPokedexNumber = 201,
    primaryAbility = "levitate",
    secondaryAbility = null,
    hiddenAbility = null,
    gen = 2,
    forms = listOf(
        Form(
            name = "unown-a"
        ),
        Form(
            name = "unown-b"
        )
    ),
    evolutions = emptyList(),
)

val pichu = Pokemon(
    name = "pichu",
    nationalPokedexNumber = 172,
    primaryAbility = "static",
    secondaryAbility = null,
    hiddenAbility = "lightning-rod",
    gen = 4,
    forms = listOf(
        Form(
            name = "spiky-eared-pichu"
        ),
    ),
    evolutions = listOf(
        Evolution(
            name = "pikachu",
            method = UseItem(item = "thunder-stone")
        )
    ),
)

fun loadFixture() = listOf(
    ivysaur,
    venusaur,
    eevee,
    unown,
    giratina,
    pichu,
)
    .forEach { PokemonTable.insert(it) }
