package com.github.aleperaltabazas.dex.config

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.name.Names

object TestContext {
    val injector: Injector = Guice.createInjector(
        CacheModule(),
        ConfigModule(),
        ConnectionMockModule(),
        ControllerModule(),
        DatabaseMockModule(),
        FileSystemMockModule(),
        HashMockModule(),
        JsonModule(),
        ServiceModule(),
        StorageModule(),
    )

    inline fun <reified T> get(name: String): T = injector.getInstance(Key.get(T::class.java, Names.named(name)))
}