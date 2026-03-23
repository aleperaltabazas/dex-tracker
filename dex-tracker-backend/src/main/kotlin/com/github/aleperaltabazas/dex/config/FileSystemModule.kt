package com.github.aleperaltabazas.dex.config

import com.github.aleperaltabazas.dex.utils.FileSystemHelper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class FileSystemModule {
    @Bean
    open fun fileSystemHelper() = FileSystemHelper()
}