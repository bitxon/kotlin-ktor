package bitxon.ktor.plugins

import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database

fun Application.configureExposed() {

    val env = environment.config.property("ktor.environment").getString()
    val dbConfig = environment.config.config("ktor.$env.db")

    val datasource = HikariDataSource().apply {
        jdbcUrl = dbConfig.property("url").getString()
        username = dbConfig.property("user").getString()
        password = dbConfig.property("password").getString()
        driverClassName = dbConfig.property("driver").getString()
        schema = dbConfig.propertyOrNull("schema")?.getString()
        maximumPoolSize = dbConfig.propertyOrNull("maxPoolSize")?.getString()?.toInt() ?: 5
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    }

    Flyway.configure().dataSource(datasource).load().migrate()
    Database.connect(datasource)
}