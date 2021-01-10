package com.github.aleperaltabazas.dex.storage

import arrow.core.Either
import arrow.core.extensions.fx
import com.github.aleperaltabazas.dex.db.model.Pokemon
import com.github.aleperaltabazas.dex.db.reifying
import com.github.aleperaltabazas.dex.db.schema.Pokemons
import com.github.aleperaltabazas.dex.extension.catchBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class PokemonStorage(
    private val db: Database,
    private val evolutionStorage: EvolutionStorage,
    private val formStorage: FormStorage,
) {
    fun findAll(): Either<Throwable, List<Pokemon>> = Either.catchBlocking {
        transaction(db) {
            Pokemons.reifying { selectAll() }.toList()
        }
    }

    fun find(dexNumbers: List<Int>): Either<Throwable, List<Pokemon>> = Either.catchBlocking {
        transaction(db) {
            Pokemons.reifying { select { Pokemons.nationalDexNumber inList dexNumbers } }
        }.toList()
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
}