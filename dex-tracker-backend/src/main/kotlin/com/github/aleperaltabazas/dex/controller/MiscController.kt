package com.github.aleperaltabazas.dex.controller

import com.github.aleperaltabazas.dex.env.Env
import spark.Spark.after
import spark.Spark.options

class MiscController(
    private val env: Env,
    private val corsOrigins: List<String>,
) : Controller {
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

        after("/api/v1/*") { req, res ->
            if (env == Env.DEV) {
                if (req.headers("Origin") in corsOrigins) {
                    res.header("Access-Control-Allow-Origin", req.headers("Origin"))
                }

                res.header("Access-Control-Allow-Credentials", "true")
            }

            res.header("content-type", "application/json")
        }
    }
}