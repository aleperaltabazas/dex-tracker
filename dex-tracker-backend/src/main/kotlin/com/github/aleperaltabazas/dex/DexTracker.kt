package com.github.aleperaltabazas.dex

import com.github.aleperaltabazas.dex.cache.Cache
import com.github.aleperaltabazas.dex.config.*
import com.github.aleperaltabazas.dex.constants.*
import com.github.aleperaltabazas.dex.controller.Controller
import com.github.aleperaltabazas.dex.filter.LoggingFilter
import com.github.aleperaltabazas.dex.filter.TrackingFilter
import com.google.inject.Guice
import com.google.inject.Injector
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.FilterHolder
import org.eclipse.jetty.servlet.ServletContextHandler
import org.slf4j.LoggerFactory
import spark.Spark.staticFiles
import spark.servlet.SparkApplication
import spark.servlet.SparkFilter
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    DexTracker().run(args)
}

class DexTracker {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(DexTracker::class.java)

        private const val CONTEXT_PATH = "/"
    }

    fun run(args: Array<String>) {
        LOGGER.info("Args passed: ${args.joinToString(", ")}")
        val port = ProcessBuilder().environment()
            ?.get("PORT")
            ?.toInt() ?: 9290
        LOGGER.info("Using port $port.")

        val handler = ServletContextHandler()
        handler.contextPath = CONTEXT_PATH

        listOf(
            object : FilterHolder(SparkFilter()) {
                init {
                    this.setInitParameter("applicationClass", App::class.java.name)
                }
            }
        ).forEach { handler.addFilter(it, "*", null) }

        try {
            val server = Server(port)
            server.handler = handler
            server.start()
            server.join()
        } catch (e: Exception) {
            LOGGER.error("Error starting the application", e)
            exitProcess(-1)
        }

        LOGGER.info("App up and running!")
    }

    class App : SparkApplication {
        private val controllers: MutableList<Controller> = mutableListOf()
        private val caches: MutableList<Cache<*>> = mutableListOf()

        override fun init() {
            val injector = Guice.createInjector(
                CacheModule(),
                ConfigModule(),
                ConnectionModule(),
                ControllerModule(),
                DatabaseModule(),
                EnvironmentModule(),
                FileSystemModule(),
                HashModule(),
                JsonModule(),
                MapperModule(),
                ServiceModule(),
                StorageModule(),
            )

            startCaches(injector)
            registerControllers(injector)
        }

        override fun destroy() = caches.forEach { it.stop() }

        private fun registerControllers(injector: Injector) {
            staticFiles.location("/templates")
            staticFiles.header(CONTENT_ENCODING, GZIP)
            staticFiles.header(KEEP_ALIVE, "timeout=5, max=1000")
            staticFiles.header(CACHE_CONTROL, MAX_AGE_1_YEAR)

            injector.allBindings.keys
                .filter { Controller::class.java.isAssignableFrom(it.typeLiteral.rawType) }
                .forEach {
                    val controller = injector.getInstance(it) as Controller
                    controller.register()
                    LOGGER.info("Registered controller ${controller.javaClass.simpleName}")
                    this.controllers.add(controller)
                }

            TrackingFilter.register()
            LoggingFilter.register()
        }

        private fun startCaches(injector: Injector) = injector.allBindings.keys
            .filter { Cache::class.java.isAssignableFrom(it.typeLiteral.rawType) }
            .forEach {
                val cache = injector.getInstance(it) as Cache<*>
                LOGGER.info("Starting ${cache.name}...")
                cache.start()
                LOGGER.info("Started cache ${cache.name}")
                this.caches.add(cache)
            }
    }
}