package com.github.aleperaltabazas.dex.functions

import com.github.aleperaltabazas.dex.DexTracker
import java.util.*

private val properties = lazy {
    val props = Properties()
    props.load(DexTracker.javaClass.getResourceAsStream("/application.properties"))
    props
}

fun version(): String = lazy { properties.value["version"] as String }.value

fun baseDir(): String = lazy { properties.value["basedir"] as String }.value
