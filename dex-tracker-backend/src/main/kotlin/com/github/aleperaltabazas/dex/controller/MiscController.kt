package com.github.aleperaltabazas.dex.controller

import spark.Spark

class MiscController : Controller {
    override fun register() {
        Spark.after("/api/v1/*") { _, res -> res.header("content-type", "application/json") }
    }
}