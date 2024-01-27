package bitxon.ktor.db.factory

import bitxon.ktor.db.model.AccountTable
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init(config: ApplicationConfig) {
        val devMode = config.property("ktor.development").getString().toBoolean()
        val dbConfig = when (devMode) {
            true -> embeddedH2()
            false -> config.config("db")
        }

        val datasource = hikari(dbConfig)
        val database = Database.connect(datasource)

        transaction(database) {
            SchemaUtils.create(AccountTable)
        }
    }

    private fun hikari(config: ApplicationConfig) = HikariDataSource().apply {
        jdbcUrl = config.property("url").getString()
        username = config.property("user").getString()
        password = config.property("password").getString()
        driverClassName = config.property("driver").getString()
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