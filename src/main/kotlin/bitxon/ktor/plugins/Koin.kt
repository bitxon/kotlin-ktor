package bitxon.ktor.plugins

import bitxon.ktor.service.AccountService
import bitxon.ktor.service.AccountServiceImpl
import io.ktor.server.application.*
import org.koin.dsl.*
import org.koin.ktor.plugin.*
import org.koin.logger.*


fun Application.configureKoin() {
    val koinContext = module {
        single<AccountService> { AccountServiceImpl() }
    }

    install(KoinIsolated) {
        slf4jLogger()
        modules(koinContext)
    }


}