package com.github.aleperaltabazas.dex.service

import com.github.aleperaltabazas.dex.storage.UsersStorage

class UsersService(
    private val usersStorage: UsersStorage
) {
    fun findUser(token: String) = usersStorage.findByToken(token)

    fun createUser() = usersStorage.createUser()
}