package com.github.aleperaltabazas.dex.db.model

enum class Type {
    FIRE,
    WATER,
    GRASS,
    ELECTRIC,
    ROCK,
    POISON,
    PSYCHIC,
    GROUND,
    ICE,
    DRAGON,
    GHOST,
    FLYING,
    BUG,
    FIGHTING,
    NORMAL,
    DARK,
    STEEL,
    FAIRY,
}

data class Pokemon(
    val id: Long?,
    val name: String,
    val nationalPokedexNumber: Int,
    val primaryAbility: String,
    val secondaryAbility: String?,
    val hiddenAbility: String?,
    val primaryType: Type,
    val secondaryType: Type?,
    val genderRatio: GenderRatio?,
    val baseStats: Stats,
    val evolutions: List<Evolution>,
    val forms: List<Form>,
)

data class GenderRatio(
    val male: Double,
    val female: Double,
)

data class Stats(
    val id: Long?,
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val specialAttack: Int,
    val specialDefense: Int,
    val speed: Int,
)

data class Form(
    val id: Long?,
    val name: String,
    val stats: Stats?,
)
