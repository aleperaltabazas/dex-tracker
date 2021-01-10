package com.github.aleperaltabazas.dex.db.schema

import com.github.aleperaltabazas.dex.db.GenericTable
import com.github.aleperaltabazas.dex.db.model.*
import com.github.aleperaltabazas.dex.db.reifying
import com.github.aleperaltabazas.dex.extension.both
import com.github.aleperaltabazas.dex.extension.fold
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select

object Pokemons : GenericTable<Pokemon>("pokemons") {
    val name = varchar("name", length = 50).uniqueIndex()
    val nationalDexNumber = integer("national_dex_number").uniqueIndex()
    val primaryAbility = varchar("primary_ability", length = 50)
    val secondaryAbility = varchar("secondary_ability", length = 50).nullable()
    val hiddenAbility = varchar("hidden_ability", length = 50).nullable()
    val primaryType = varchar("primary_type", length = 8)
    val secondaryType = varchar("secondary_type", length = 8).nullable()
    val maleProbability = double("male_prob").nullable()
    val femaleProbability = double("female_prob").nullable()
    val hp = integer("hp")
    val attack = integer("attack")
    val defense = integer("defense")
    val specialAttack = integer("special_attack")
    val specialDefense = integer("special_defense")
    val speed = integer("speed")

    override fun reify(row: ResultRow): Pokemon = Pokemon(
        id = row[id],
        name = row[name],
        nationalPokedexNumber = row[nationalDexNumber],
        primaryAbility = row[primaryAbility],
        secondaryAbility = row[secondaryAbility],
        hiddenAbility = row[hiddenAbility],
        primaryType = Type.valueOf(row[primaryType]),
        secondaryType = row[secondaryType]?.let { Type.valueOf(it) },
        genderRatio = both({ row[maleProbability] }, { row[femaleProbability] })?.fold { male, female ->
            GenderRatio(male = male, female = female)
        },
        baseStats = Stats(
            id = null,
            hp = row[hp],
            attack = row[attack],
            defense = row[defense],
            specialAttack = row[specialAttack],
            specialDefense = row[specialDefense],
            speed = row[speed],
        ),
        evolutions = Evolutions.reifying {
            select { Evolutions.pokemonId eq row[id] }
        }.toList(),
        forms = Forms.reifying {
            select { Forms.pokemonId eq row[id] }
        }.toList()
    )
}

object Forms : GenericTable<Form>("forms") {
    val name = varchar("name", length = 30).uniqueIndex()
    val pokemonId = long("pokemon_id") references Pokemons.id
    val statsId = (long("stats_id") references Statses.id).nullable()

    override fun reify(row: ResultRow): Form = Form(
        id = row[id],
        name = row[name],
        stats = row[statsId]?.let {
            Statses.reifying {
                select { Statses.id eq it }
            }
        }?.firstOrNull(),
    )
}

object Statses : GenericTable<Stats>("stats") {
    val hp = integer("hp")
    val attack = integer("attack")
    val defense = integer("defense")
    val specialAttack = integer("special_attack")
    val specialDefense = integer("special_defense")
    val speed = integer("speed")

    override fun reify(row: ResultRow): Stats = Stats(
        id = row[id],
        hp = row[hp],
        attack = row[attack],
        defense = row[defense],
        specialAttack = row[specialAttack],
        specialDefense = row[specialDefense],
        speed = row[speed],
    )
}

object Evolutions : GenericTable<Evolution>("evolutions") {
    val name = varchar("name", length = 50).uniqueIndex()
    val pokemonId = long("pokemon_id") references Pokemons.id
    val method = varchar("method", length = 50)

    override fun reify(row: ResultRow): Evolution = Evolution(
        id = row[id],
        name = row[name],
        method = EvolutionMethod.parse(row[method])
            ?: throw IllegalArgumentException("Failed to parse evolution method ${row[method]}")
    )
}
