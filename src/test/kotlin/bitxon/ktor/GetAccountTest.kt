package bitxon.ktor

import bitxon.ktor.api.model.Account
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test
import kotlinx.datetime.LocalDate

class GetAccountTest {

    companion object {
        private val ACCOUNT_1 = Account(
            id = 1,
            email = "alice@mail.com",
            firstName = "Alice",
            lastName = "Anderson",
            dateOfBirth = LocalDate(1991, 1, 21),
            currency = "USD",
            moneyAmount = 340
        )
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
        assertThat(response.body<List<Account>>()).isNotEmpty().contains(ACCOUNT_1)
    }

    @Test
    fun getById() = testApplication {
        // config
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        // when
        val response = client.get("/accounts/${ACCOUNT_1.id}")

        // then
        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        assertThat(response.body<Account>())
            .usingRecursiveComparison()
            .isEqualTo(ACCOUNT_1)
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
