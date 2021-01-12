package com.github.aleperaltabazas.dex.storage

import com.github.aleperaltabazas.dex.db.dao.PokemonDAO
import com.github.aleperaltabazas.dex.db.schema.PokemonTable
import com.github.aleperaltabazas.dex.model.Pokemon
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

class PokemonStorage(
    private val evolutionStorage: EvolutionStorage,
    private val formStorage: FormStorage,
    db: Database,
) : Storage<PokemonDAO, Pokemon>(db, PokemonDAO) {
    override fun save(e: Pokemon): PokemonDAO {
        return transaction(db) {
            val dao = PokemonDAO.new {
                this.name = e.name
                this.nationalPokedexNumber = e.nationalPokedexNumber
                this.primaryAbility = e.primaryAbility
                this.secondaryAbility = e.secondaryAbility
                this.hiddenAbility = e.hiddenAbility
                this.primaryType = e.typing.primaryType.name
                this.secondaryType = e.typing.secondaryType?.name
                this.maleProbability = e.genderRatio?.male
                this.femaleProbability = e.genderRatio?.female
                this.hp = e.baseStats.hp
                this.attack = e.baseStats.attack
                this.defense = e.baseStats.defense
                this.specialAttack = e.baseStats.specialAttack
                this.specialDefense = e.baseStats.specialDefense
                this.speed = e.baseStats.speed
                this.gen = e.gen
            }

            e.evolutions.forEach { evolutionStorage.save(it) }
            e.forms.forEach { formStorage.save(it) }

            dao
        }
    }

    override fun toModel(dao: PokemonDAO): Pokemon = Pokemon(dao)
}