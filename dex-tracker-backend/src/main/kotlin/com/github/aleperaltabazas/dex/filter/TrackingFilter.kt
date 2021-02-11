package com.github.aleperaltabazas.dex.filter

import org.apache.commons.lang3.RandomStringUtils
import org.slf4j.MDC
import spark.Spark.before

object ThreadMap {
    private val state = ThreadLocal.withInitial<MutableMap<String, String>> { HashMap() }

    fun get(): Map<String, String>? {
        return state.get()
    }

    fun put(key: String, value: String) {
        state.get()[key] = value
        MDC.put(key, value)
    }
}

object TrackingFilter {
    fun register() {
        before({ req, _ ->
            val requestId = RandomStringUtils.randomAlphanumeric(8)
            val client = req.headers("dex-client") ?: lazy { "dex-" + RandomStringUtils.randomAlphanumeric(8) }.value

            ThreadMap.put("requestId", requestId)
            ThreadMap.put("dexClient", client)
            ThreadMap.put(
                "motive",
                if (req.pathInfo().startsWith("/api/v1")) "api" else "web"
            )
        })
    }
}