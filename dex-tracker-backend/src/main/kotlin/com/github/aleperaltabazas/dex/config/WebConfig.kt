package com.github.aleperaltabazas.dex.config

import com.github.aleperaltabazas.dex.filter.LoggingFilter
import com.github.aleperaltabazas.dex.filter.TrackingFilter
import com.github.aleperaltabazas.dex.utils.Environment
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
open class WebConfig(
    private val loggingFilter: LoggingFilter,
    private val trackingFilter: TrackingFilter,
    private val env: Environment,
    @Value("#{'\${app.cors.origins}'.split(',')}") private val corsOrigins: List<String>,
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(trackingFilter)
        registry.addInterceptor(loggingFilter)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        if (env.isDev()) {
            registry.addMapping("/api/v1/**")
                .allowedOrigins(*corsOrigins.toTypedArray())
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowCredentials(true)
        }
    }
}
