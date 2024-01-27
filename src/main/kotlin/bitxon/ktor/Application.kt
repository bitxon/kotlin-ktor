package bitxon.ktor

import bitxon.ktor.db.factory.DatabaseFactory
import bitxon.ktor.plugins.*
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureLogging()
    configureKoin()
    configureErrorHandling()
    configureSerialization()
    configureRouting()
    DatabaseFactory.init(environment.config)
}
