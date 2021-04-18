package com.github.aleperaltabazas.dex.utils

import com.github.aleperaltabazas.dex.constants.*
import com.github.aleperaltabazas.dex.functions.baseDir
import spark.Spark

interface StaticFilesResolver {
    fun register()
}

object ClasspathResolver : StaticFilesResolver {
    override fun register() {
        Spark.staticFiles.location("/assets/static")
        Spark.staticFiles.header(CONTENT_ENCODING, GZIP)
        Spark.staticFiles.header(KEEP_ALIVE, "timeout=5, max=1000")
        Spark.staticFiles.header(CACHE_CONTROL, MAX_AGE_1_YEAR)
    }
}

object FileSystemResolver : StaticFilesResolver {
    override fun register() {
        Spark.staticFiles.externalLocation("${baseDir()}/src/main/resources/assets/static")
    }
}

object FakeResolver : StaticFilesResolver {
    override fun register() = Unit
}
