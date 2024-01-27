package bitxon.ktor.plugins

import bitxon.ktor.exception.ResourceNotFoundException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureErrorHandling() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            when (cause) {
                is IllegalArgumentException -> call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = mapOf("message" to "$cause")
                )

                is ResourceNotFoundException -> call.respond(
                    status = HttpStatusCode.NotFound,
                    message = mapOf("message" to "${cause.message}")
                )

                is BadRequestException -> call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = mapOf("message" to "${cause.message}", "causedBy" to "${cause.cause?.message}")
                )

                else -> call.respond(
                    status = HttpStatusCode.InternalServerError,
                    message = mapOf("message" to "$cause", "causedBy" to "${cause.cause}")
                )
            }
        }
    }
}