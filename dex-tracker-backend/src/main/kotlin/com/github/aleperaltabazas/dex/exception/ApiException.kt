package com.github.aleperaltabazas.dex.exception

open class ApiException(val status: Int, override val message: String) : RuntimeException(message)

class BadRequestException(message: String) : ApiException(400, message)
class UnauthorizedException(message: String) : ApiException(401, message)
class ForbiddenException(message: String) : ApiException(403, message)
class NotFoundException(message: String) : ApiException(404, message)
