package bitxon.ktor

import bitxon.ktor.api.model.Account
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlinx.datetime.LocalDate
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test


class CreateAccountTest {

    @Test
    fun createAccount() = testApplication {
        // config
        environment { config = ApplicationConfig("application.yaml") }
        val client = createClient { install(ContentNegotiation) { json() } }

        // given
        val accountToCreate = Account(
            email = "bob@mail.com",
            firstName = "Bob",
            lastName = "Green",
            dateOfBirth = LocalDate(1991, 3, 17),
            currency = "USD",
            moneyAmount = 134
        )

        // when
        val response = client.post("/accounts") {
            contentType(ContentType.Application.Json)
            setBody(accountToCreate)
        }

        // then
        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        assertThat(response.body<Account>().id).isNotNull();
        assertThat(response.body<Account>())
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(accountToCreate)
    }
}
