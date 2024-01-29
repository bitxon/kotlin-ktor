package bitxon.ktor.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import org.slf4j.event.*

fun Application.configureLogging() {
    install(CallLogging) {
        level = Level.INFO
        format { call ->
            val httpMethod = call.request.httpMethod.value
            val path = call.request.path()
            val status = call.response.status()
            val time = call.processingTimeMillis()
            "$httpMethod $path --> $status @${time}ms"
        }
    }


    environment.monitor.subscribe(ApplicationStarted) { application ->
        val env = application.environment.config.property("ktor.environment").getString();
        application.environment.log.info("Application is started on '$env' environment")
    }
    environment.monitor.subscribe(ApplicationStopped) { application ->
        application.environment.log.info("Application is stopped")
        // Release resources and unsubscribe from events
        application.environment.monitor.unsubscribe(ApplicationStarted) {}
        application.environment.monitor.unsubscribe(ApplicationStopped) {}
    }
}