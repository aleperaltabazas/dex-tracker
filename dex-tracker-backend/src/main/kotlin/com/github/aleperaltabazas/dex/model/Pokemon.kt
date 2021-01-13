package com.github.aleperaltabazas.dex.model

import com.github.aleperaltabazas.dex.db.dao.FormDAO
import com.github.aleperaltabazas.dex.db.dao.PokemonDAO

enum class Type {
    BUG,
    DARK,
    DRAGON,
    ELECTRIC,
    FAIRY,
    FIGHTING,
    FIRE,
    FLYING,
    GHOST,
    GRASS,
    GROUND,
    ICE,
    NORMAL,
    POISON,
    PSYCHIC,
    ROCK,
    STEEL,
    WATER,
}

data class Pokemon(
    val name: String,
    val nationalPokedexNumber: Int,
    val primaryAbility: String,
    val secondaryAbility: String?,
    val hiddenAbility: String?,
    val evolutions: List<Evolution>,
    val forms: List<Form>,
    val gen: Int,
) {
    constructor(dao: PokemonDAO) : this(
        name = dao.name,
        nationalPokedexNumber = dao.nationalPokedexNumber,
        primaryAbility = dao.primaryAbility,
        secondaryAbility = dao.secondaryAbility,
        hiddenAbility = dao.hiddenAbility,
        evolutions = dao.evolutions.map { Evolution(it) },
        forms = dao.forms.map { Form(it) },
        gen = dao.gen,
    )
}

data class Typing(
    val primaryType: Type,
    val secondaryType: Type?,
)

data class GenderRatio(
    val male: Double,
    val female: Double,
)

data class Stats(
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val specialAttack: Int,
    val specialDefense: Int,
    val speed: Int,
)

data class Form(
    val name: String,
) {
    constructor(dao: FormDAO) : this(
        name = dao.name,
    )
}
