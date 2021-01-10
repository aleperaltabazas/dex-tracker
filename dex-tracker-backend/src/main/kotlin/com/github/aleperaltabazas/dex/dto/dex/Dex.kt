package com.github.aleperaltabazas.dex.dto.dex

data class DexEntryDTO(
    val name: String,
    val number: Int,
)

data class GameDTO(
    val title: String,
    val fullTitle: String,
    val spritePokemon: String,
)

data class RegionalPokedexDTO(
    val pokemons: List<DexEntryDTO>,
    val region: String?,
    val type: String,
    val game: GameDTO,
)

data class PokemonDTO(
    val number: Int,
    val name: String,
    val primaryAbility: String,
    val secondaryAbility: String?,
    val hiddenAbility: String?,
    val genderRatio: Pair<Double, Double>?,
)

data class FormDTO(
    val pokemon: String,
    val name: String,
)
