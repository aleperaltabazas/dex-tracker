package com.github.aleperaltabazas.dex.config

import com.github.aleperaltabazas.dex.functions.baseDir
import com.github.aleperaltabazas.dex.utils.Environment
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import freemarker.cache.ClassTemplateLoader
import freemarker.template.Configuration
import freemarker.template.Version
import org.slf4j.LoggerFactory
import spark.TemplateEngine
import spark.template.freemarker.FreeMarkerEngine
import java.io.File

class TemplatingModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("templateEngine")
    fun templateEngine(
        @Named("env") env: Environment
    ): TemplateEngine = FreeMarkerEngine(
        when (env) {
            Environment.DEV -> hotReloadConfiguration()
            else -> defaultConfiguration()
        }
    )

    private fun hotReloadConfiguration(): Configuration {
        val config = Configuration(Version("2.3.0"))
        config.setDirectoryForTemplateLoading(File("${baseDir()}/src/main/resources/assets/templates"))

        return config
    }

    private fun defaultConfiguration(): Configuration {
        val config = Configuration(Version("2.3.0"))
        config.templateLoader = ClassTemplateLoader(this.javaClass, "/assets/templates")

        return config
    }
}