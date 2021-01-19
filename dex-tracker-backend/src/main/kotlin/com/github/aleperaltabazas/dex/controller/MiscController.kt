package com.github.aleperaltabazas.dex.controller

import spark.Spark.after

class MiscController : Controller {
    override fun register() {
        after("/api/v1/*") { _, res ->
            res.header("Access-Control-Allow-Origin", "http://localhost:8080")
            res.header("Access-Control-Allow-Credentials", "true")
            res.header("content-type", "application/json")
        }
    }
}