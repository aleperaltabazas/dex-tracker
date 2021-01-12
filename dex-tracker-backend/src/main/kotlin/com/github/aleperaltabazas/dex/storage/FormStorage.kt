package com.github.aleperaltabazas.dex.storage

import com.github.aleperaltabazas.dex.db.dao.FormDAO
import com.github.aleperaltabazas.dex.model.Form
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

class FormStorage(
    db: Database,
) : Storage<FormDAO, Form>(db, FormDAO) {
    override fun save(m: Form): FormDAO = transaction(db) {
        FormDAO.new {
            this.name = m.name
            this.hp = m.stats.hp
            this.attack = m.stats.attack
            this.defense = m.stats.defense
            this.specialAttack = m.stats.specialAttack
            this.specialDefense = m.stats.specialDefense
            this.speed = m.stats.speed
            this.primaryType = m.typing.primaryType.name
            this.secondaryType = m.typing.secondaryType?.name
        }
    }

    override fun toModel(dao: FormDAO): Form = Form(dao)
}