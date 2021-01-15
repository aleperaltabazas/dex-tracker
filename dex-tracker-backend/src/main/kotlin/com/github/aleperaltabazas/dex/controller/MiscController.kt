package com.github.aleperaltabazas.dex.controller

import spark.Spark.after

class MiscController : Controller {
    override fun register() {
        after("/api/v1/*") { _, res -> res.header("content-type", "application/json") }
    }
}