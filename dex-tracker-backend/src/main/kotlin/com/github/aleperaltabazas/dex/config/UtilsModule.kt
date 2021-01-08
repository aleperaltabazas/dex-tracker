package com.github.aleperaltabazas.dex.config

import com.github.aleperaltabazas.dex.utils.FileSystemHelper
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named

open class UtilsModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("fileSystemHelper")
    open fun fileSystemHelper() = FileSystemHelper()
}