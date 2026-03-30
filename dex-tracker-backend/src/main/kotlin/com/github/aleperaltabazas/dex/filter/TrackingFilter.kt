package com.github.aleperaltabazas.dex.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.lang3.RandomStringUtils
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class TrackingFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val requestId = RandomStringUtils.randomAlphanumeric(8)
        val client = request.getHeader("dex-client") ?: "dex-" + RandomStringUtils.randomAlphanumeric(8)

        MDC.put("requestId", requestId)
        MDC.put("dexClient", client)
        MDC.put("motive", if (request.requestURI.startsWith("/api/v1")) "api" else "web")

        try {
            filterChain.doFilter(request, response)
        } finally {
            MDC.clear()
        }
    }
}
