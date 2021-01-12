package com.github.aleperaltabazas.dex.dto.dex

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

data class PokemonDTO(
    val name: String,
    val nationalPokedexNumber: Int,
    val primaryAbility: String,
    val secondaryAbility: String?,
    val hiddenAbility: String?,
    val typing: TypingDTO,
    val genderRatio: GenderRatioDTO?,
    val baseStats: StatsDTO,
    val evolutions: List<EvolutionDTO>,
    val forms: List<FormDTO>,
)

data class TypingDTO(
    val primaryType: Type,
    val secondaryType: Type?,
)

data class GenderRatioDTO(
    val male: Double,
    val female: Double,
)

data class StatsDTO(
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val specialAttack: Int,
    val specialDefense: Int,
    val speed: Int,
)

data class FormDTO(
    val name: String,
    val stats: StatsDTO?,
    val typing: TypingDTO,
)
