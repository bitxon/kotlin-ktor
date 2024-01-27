package bitxon.ktor.exception

class ResourceNotFoundException(message: String, cause: Throwable? = null) : Exception(message, cause)