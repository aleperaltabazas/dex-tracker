package com.github.aleperaltabazas.dex.exception

open class ApiException(val status: Int, override val message: String) : RuntimeException(message)

class BadRequestException(message: String) : ApiException(400, message)
class NotFoundException(message: String) : ApiException(404, message)
