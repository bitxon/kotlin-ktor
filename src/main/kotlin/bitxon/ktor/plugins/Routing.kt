package bitxon.ktor.plugins

import bitxon.ktor.api.model.Account
import bitxon.ktor.service.AccountService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    val accountService by inject<AccountService>()

    routing {
        route("/accounts") {
            get("") {
                val results = accountService.getAll()
                call.respond(results)
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toLongOrNull() ?: throw IllegalArgumentException("Invalid Id")
                val result = accountService.getById(id)
                call.respond(result)
            }

            post("") {
                val account = call.receive<Account>()
                val result = accountService.create(account)
                call.respond(result)
            }
        }
    }
}
