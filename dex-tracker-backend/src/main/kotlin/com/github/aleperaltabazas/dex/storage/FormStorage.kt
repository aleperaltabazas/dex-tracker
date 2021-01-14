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
        }
    }

    override fun toModel(dao: FormDAO): Form = Form(
        name = dao.name,
    )
}