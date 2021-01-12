package com.github.aleperaltabazas.dex.storage

import arrow.core.Either
import arrow.core.extensions.fx
import com.github.aleperaltabazas.dex.db.model.GenderRatio
import com.github.aleperaltabazas.dex.db.model.Pokemon
import com.github.aleperaltabazas.dex.db.model.Stats
import com.github.aleperaltabazas.dex.db.model.Type
import com.github.aleperaltabazas.dex.db.schema.Pokemons
import com.github.aleperaltabazas.dex.extension.both
import com.github.aleperaltabazas.dex.extension.catchBlocking
import com.github.aleperaltabazas.dex.extension.fold
import com.github.aleperaltabazas.dex.extension.sequence
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class PokemonStorage(
    private val db: Database,
    private val evolutionStorage: EvolutionStorage,
    private val formStorage: FormStorage,
) {
    fun findAll(): Either<Throwable, List<Pokemon>> = transaction(db) {
        Either.fx {
            Pokemons.selectAll()
                .asSequence()
                .map(this@PokemonStorage::toDomain)
                .toList()
                .sequence()
                .bind()
        }
    }

    fun save(pokemon: Pokemon): Either<Throwable, Long> = Either.fx {
        val id = Either.catchBlocking {
            transaction(db) {
                Pokemons.insert { row ->
                    row[name] = pokemon.name
                    row[nationalDexNumber] = pokemon.nationalPokedexNumber
                    row[primaryAbility] = pokemon.primaryAbility
                    row[secondaryAbility] = pokemon.secondaryAbility
                    row[hiddenAbility] = pokemon.hiddenAbility
                    row[primaryType] = pokemon.primaryType.name
                    row[secondaryType] = pokemon.secondaryType?.name
                    row[maleProbability] = pokemon.genderRatio?.male
                    row[femaleProbability] = pokemon.genderRatio?.female
                    row[hp] = pokemon.baseStats.hp
                    row[attack] = pokemon.baseStats.attack
                    row[defense] = pokemon.baseStats.defense
                    row[specialAttack] = pokemon.baseStats.specialAttack
                    row[specialDefense] = pokemon.baseStats.specialDefense
                    row[speed] = pokemon.baseStats.speed
                } get Pokemons.id
            }
        }.bind()

        pokemon.forms.forEach { formStorage.save(it, id).bind() }
        pokemon.evolutions.forEach { evolutionStorage.save(it, id).bind() }

        id
    }

    private fun toDomain(row: ResultRow): Either<Throwable, Pokemon> = Either.fx<Throwable, Pokemon> {
        val evolutions = evolutionStorage.findForPokemon(row[Pokemons.id]).bind()
        val forms = formStorage.findForPokemon(row[Pokemons.id]).bind()

        Pokemon(
            id = row[Pokemons.id],
            name = row[Pokemons.name],
            nationalPokedexNumber = row[Pokemons.nationalDexNumber],
            primaryAbility = row[Pokemons.primaryAbility],
            secondaryAbility = row[Pokemons.secondaryAbility],
            hiddenAbility = row[Pokemons.hiddenAbility],
            primaryType = Type.valueOf(row[Pokemons.primaryType]),
            secondaryType = row[Pokemons.secondaryType]?.let { Type.valueOf(it) },
            genderRatio = both({ row[Pokemons.maleProbability] }, { row[Pokemons.femaleProbability] })?.fold { male, female ->
                GenderRatio(male = male, female = female)
            },
            baseStats = Stats(
                id = null,
                hp = row[Pokemons.hp],
                attack = row[Pokemons.attack],
                defense = row[Pokemons.defense],
                specialAttack = row[Pokemons.specialAttack],
                specialDefense = row[Pokemons.specialDefense],
                speed = row[Pokemons.speed],
            ),
            evolutions = evolutions,
            forms = forms,
        )
    }
}