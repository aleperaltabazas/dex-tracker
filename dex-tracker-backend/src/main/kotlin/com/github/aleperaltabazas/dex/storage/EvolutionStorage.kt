package com.github.aleperaltabazas.dex.storage

import com.github.aleperaltabazas.dex.db.dao.EvolutionDAO
import com.github.aleperaltabazas.dex.model.Evolution
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

class EvolutionStorage(
    db: Database
) : Storage<EvolutionDAO, Evolution>(db, EvolutionDAO) {
    override fun save(m: Evolution): EvolutionDAO = transaction(db) {
        EvolutionDAO.new {
            this.name = m.name
            this.method = m.method
        }
    }

    override fun toModel(dao: EvolutionDAO): Evolution = Evolution(dao)
}