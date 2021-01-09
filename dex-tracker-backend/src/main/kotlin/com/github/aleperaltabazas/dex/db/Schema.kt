package com.github.aleperaltabazas.dex.db

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll

object PokemonsTable : Table("pokemons") {
    val id = long("id").autoIncrement()
    val name = varchar("name", length = 50)
    val nationalDexNumber = integer("national_dex_number")
    val primaryAbility = varchar("primary_ability", length = 50)
    val secondaryAbility = varchar("secondary_ability", length = 50).nullable()
    val hiddenAbility = varchar("hidden_ability", length = 50).nullable()
    val primaryType = varchar("primary_type", length = 8)
    val secondaryType = varchar("secondary_type", length = 8).nullable()
    val hp = integer("hp")
    val attack = integer("attack")
    val defense = integer("defense")
    val specialAttack = integer("special_attack")
    val specialDefense = integer("special_defense")
    val speed = integer("speed")

    override val primaryKey = PrimaryKey(id)

    fun select(): Sequence<Pokemon> = this.selectAll()
        .asSequence()
        .map { row ->
            Pokemon(
                id = row[id],
                name = row[name],
                nationalPokedexNumber = row[nationalDexNumber],
                primaryAbility = row[primaryAbility],
                secondaryAbility = row[secondaryAbility],
                hiddenAbility = row[hiddenAbility],
                primaryType = Type.valueOf(row[primaryType]),
                secondaryType = row[secondaryType]?.let { Type.valueOf(it) },
                baseStats = Stats(
                    id = null,
                    hp = row[hp],
                    attack = row[attack],
                    defense = row[defense],
                    specialAttack = row[specialAttack],
                    specialDefense = row[specialDefense],
                    speed = row[speed],
                ),
                evolutions = EvolutionsTable.select()
                    .filter { it.name == row[name] }
                    .toList(),
                forms = FormsTable.select()
                    .filter { it.pokemonId == row[id] }
                    .toList()
            )
        }
}

object FormsTable : Table("forms") {
    val id = long("id").autoIncrement()
    val name = varchar("name", length = 30)
    val pokemonId = long("pokemon_id") references PokemonsTable.id
    val statsId = (long("stats_id") references StatsTable.id).nullable()

    override val primaryKey = PrimaryKey(PokemonsTable.id)

    fun select(): Sequence<Form> = this.selectAll()
        .asSequence()
        .map { row ->
            Form(
                id = row[id],
                name = row[name],
                stats = StatsTable.select().find { row[statsId] == it.id },
                pokemonId = row[pokemonId]
            )
        }
}

object StatsTable : Table("stats") {
    val id = long("id").autoIncrement()
    val hp = integer("hp")
    val attack = integer("attack")
    val defense = integer("defense")
    val specialAttack = integer("special_attack")
    val specialDefense = integer("special_defense")
    val speed = integer("speed")

    override val primaryKey = PrimaryKey(id)

    fun select(): Sequence<Stats> = this.selectAll()
        .asSequence()
        .map { row ->
            Stats(
                id = row[id],
                hp = row[hp],
                attack = row[attack],
                defense = row[defense],
                specialAttack = row[specialAttack],
                specialDefense = row[specialDefense],
                speed = row[speed],
            )
        }

}

object EvolutionsTable : Table("evolutions") {
    val id = long("id").autoIncrement()
    val name = varchar("name", length = 50).autoIncrement()
    val pokemonId = (long("pokemon_id") references PokemonsTable.id)
    val method = varchar("method", length = 50)

    override val primaryKey = PrimaryKey(id)

    fun select(): Sequence<Evolution> = this.selectAll()
        .asSequence()
        .map { row ->
            Evolution(
                id = row[id],
                name = row[name],
                method = EvolutionMethod.parse(row[method])
                    ?: throw IllegalArgumentException("Failed to parse evolution method ${row[method]}")
            )
        }
}
