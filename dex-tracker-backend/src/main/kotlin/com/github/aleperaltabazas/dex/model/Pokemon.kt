package com.github.aleperaltabazas.dex.model

import com.github.aleperaltabazas.dex.db.dao.FormDAO
import com.github.aleperaltabazas.dex.db.dao.PokemonDAO
import com.github.aleperaltabazas.dex.extension.both
import com.github.aleperaltabazas.dex.extension.fold

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
    val typing: Typing,
    val genderRatio: GenderRatio?,
    val baseStats: Stats,
    val evolutions: List<Evolution>,
    val forms: List<Form>,
) {
    constructor(dao: PokemonDAO) : this(
        name = dao.name,
        nationalPokedexNumber = dao.nationalPokedexNumber,
        primaryAbility = dao.primaryAbility,
        secondaryAbility = dao.secondaryAbility,
        hiddenAbility = dao.hiddenAbility,
        typing = Typing(
            primaryType = Type.valueOf(dao.primaryType),
            secondaryType = dao.secondaryType?.let { Type.valueOf(it) }
        ),
        genderRatio = both({ dao.maleProbability }, { dao.femaleProbability })?.fold { male, female ->
            GenderRatio(male, female)
        },
        baseStats = Stats(
            hp = dao.hp,
            attack = dao.attack,
            defense = dao.defense,
            specialAttack = dao.specialAttack,
            specialDefense = dao.specialDefense,
            speed = dao.speed,
        ),
        evolutions = dao.evolutions.map { Evolution(it) },
        forms = dao.forms.map { Form(it) },
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
    val stats: Stats,
    val typing: Typing,
) {
    constructor(dao: FormDAO) : this(
        name = dao.name,
        stats = Stats(
            hp = dao.hp,
            attack = dao.attack,
            defense = dao.defense,
            specialAttack = dao.specialAttack,
            specialDefense = dao.specialDefense,
            speed = dao.speed
        ),
        typing = Typing(
            primaryType = Type.valueOf(dao.primaryType),
            secondaryType = dao.secondaryType?.let { Type.valueOf(it) }
        )
    )
}
