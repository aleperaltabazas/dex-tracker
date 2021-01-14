package com.github.aleperaltabazas.dex.exception

import java.lang.RuntimeException

open class ApiException(val status: Int, message: String) : RuntimeException(message)

class BadRequestException(message: String) : ApiException(400, message)
class NotFoundException(message: String) : ApiException(404, message)
