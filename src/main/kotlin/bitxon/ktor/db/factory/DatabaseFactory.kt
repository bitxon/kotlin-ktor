package bitxon.ktor.db.factory

import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database

object DatabaseFactory {

    fun init(config: ApplicationConfig) {
        val devMode = config.property("ktor.development").getString().toBoolean()
        val dbConfig = when (devMode) {
            true -> embeddedH2()
            false -> config.config("db")
        }

        val datasource = hikari(dbConfig)

        Flyway.configure().dataSource(datasource).load().migrate()
        Database.connect(datasource)
    }

    private fun hikari(config: ApplicationConfig) = HikariDataSource().apply {
        jdbcUrl = config.property("url").getString()
        username = config.property("user").getString()
        password = config.property("password").getString()
        driverClassName = config.property("driver").getString()
        schema = config.propertyOrNull("schema")?.getString()
        maximumPoolSize = config.propertyOrNull("maxPoolSize")?.getString()?.toInt() ?: 5
        validate()
    }

    private fun embeddedH2() = MapApplicationConfig().apply {
        put("url", "jdbc:h2:mem:test2;DB_CLOSE_DELAY=-1")
        put("user", "root")
        put("password", "")
        put("driver", "org.h2.Driver")
    }

}