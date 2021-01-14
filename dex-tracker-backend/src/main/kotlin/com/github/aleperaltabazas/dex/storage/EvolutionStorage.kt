package com.github.aleperaltabazas.dex.storage

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.aleperaltabazas.dex.db.dao.EvolutionDAO
import com.github.aleperaltabazas.dex.model.Evolution
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

class EvolutionStorage(
    private val objectMapper: ObjectMapper,
    db: Database
) : Storage<EvolutionDAO, Evolution>(db, EvolutionDAO) {
    override fun save(m: Evolution): EvolutionDAO = transaction(db) {
        EvolutionDAO.new {
            this.name = m.name
            this.method = objectMapper.writeValueAsString(m.method)
        }
    }

    override fun toModel(dao: EvolutionDAO): Evolution = Evolution(
        name = dao.name,
        method = objectMapper.readValue(dao.method)
    )
}