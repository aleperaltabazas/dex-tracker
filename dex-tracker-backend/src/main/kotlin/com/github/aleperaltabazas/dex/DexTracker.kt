package com.github.aleperaltabazas.dex

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

fun main(args: Array<String>) {
    runApplication<DexTracker>(*args)
}

@SpringBootApplication
class DexTracker