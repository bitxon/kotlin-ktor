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

class GetAccountTest {

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
