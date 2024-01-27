package bitxon.ktor

import bitxon.ktor.api.model.Account
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.datetime.LocalDate
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class ApplicationTest {

    @Test
    fun createAccount() = testApplication {
        // config
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

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

    @Test
    fun getAll() = testApplication {
        // config
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        // when
        val response = client.get("/accounts");

        // then
        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        assertThat(response.body<List<Account>>()).isNotNull() // TODO assert some entities in future
    }

    @Test
    fun getByIdNotFound() = testApplication {
        // config
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        // when
        val response = client.get("/accounts/{id}") {
            parameter("id", -1)
        };

        // then
        assertThat(response.status).isEqualTo(HttpStatusCode.NotFound)
    }


    @Test
    fun invalidRoute() = testApplication {

        // when
        val response = client.get("/invalid-resource");

        // then
        assertThat(response.status).isEqualTo(HttpStatusCode.NotFound)
    }
}
