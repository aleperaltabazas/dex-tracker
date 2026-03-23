package com.github.aleperaltabazas.dex.filter

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.lang3.RandomStringUtils
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

object ThreadMap {
    private val state = ThreadLocal.withInitial<MutableMap<String, String>> { HashMap() }

    fun get(): Map<String, String>? = state.get()

    fun put(key: String, value: String) {
        state.get()[key] = value
        MDC.put(key, value)
    }
}

@Component
class TrackingFilter : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val requestId = RandomStringUtils.randomAlphanumeric(8)
        val client = request.getHeader("dex-client") ?: "dex-${RandomStringUtils.randomAlphanumeric(8)}"

        ThreadMap.put("requestId", requestId)
        ThreadMap.put("dexClient", client)
        ThreadMap.put("motive", if (request.requestURI.startsWith("/api/v1")) "api" else "web")

        return true
    }
}
