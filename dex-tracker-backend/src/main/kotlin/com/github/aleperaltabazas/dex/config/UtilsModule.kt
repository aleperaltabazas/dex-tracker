package com.github.aleperaltabazas.dex.config

import com.github.aleperaltabazas.dex.utils.ClasspathResolver
import com.github.aleperaltabazas.dex.utils.Environment
import com.github.aleperaltabazas.dex.utils.FileSystemResolver
import com.github.aleperaltabazas.dex.utils.StaticFilesResolver
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named

class UtilsModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("staticFilesResolver")
    fun resolver(@Named("env") env: Environment): StaticFilesResolver = when (env) {
        Environment.DEV -> FileSystemResolver
        else -> ClasspathResolver
    }
}