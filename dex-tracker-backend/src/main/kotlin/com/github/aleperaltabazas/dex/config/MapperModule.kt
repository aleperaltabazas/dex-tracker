package com.github.aleperaltabazas.dex.config

import com.github.aleperaltabazas.dex.mapper.ModelMapper
import com.github.aleperaltabazas.dex.service.GameService
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named

class MapperModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("modelMapper")
    fun modelMapper(
        @Named("gameService") gameService: GameService
    ) = ModelMapper(gameService)
}