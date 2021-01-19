package com.github.aleperaltabazas.dex.controller

import spark.Spark.*

class MiscController : Controller {
    override fun register() {
        options("/*"
        ) { request, response ->
            val accessControlRequestHeaders = request
                .headers("Access-Control-Request-Headers")
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders)
            }
            val accessControlRequestMethod = request
                .headers("Access-Control-Request-Method")
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod)
            }
            "OK"
        }

        before({ _, response -> response.header("Access-Control-Allow-Origin", "*") })
        after("/api/v1/*") { _, res -> res.header("content-type", "application/json") }
    }
}