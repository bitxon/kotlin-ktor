package bitxon.ktor;

import bitxon.ktor.api.model.Account
import bitxon.ktor.api.model.Transfer
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.datetime.LocalDate
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class TransferTest {

    @Test
    fun transfer() = testApplication {
        // config
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        // given
        val senderMoneyAmount = 79
        val recipientMoneyAmount = 33
        val transferMoneyAmount = 40

        val sender = client.post("/accounts") {
            contentType(ContentType.Application.Json)
            setBody(
                Account(
                    email = "alice@mail.com",
                    firstName = "Alice",
                    lastName = "N/A",
                    dateOfBirth = LocalDate(1991, 3, 17),
                    currency = "USD",
                    moneyAmount = senderMoneyAmount
                )
            )
        }.body<Account>()

        val recipient = client.post("/accounts") {
            contentType(ContentType.Application.Json)
            setBody(
                Account(
                    email = "bob@mail.com",
                    firstName = "Bob",
                    lastName = "N/A",
                    dateOfBirth = LocalDate(1991, 3, 17),
                    currency = "USD",
                    moneyAmount = recipientMoneyAmount
                )
            )
        }.body<Account>()


        // when
        val response = client.post("/accounts/transfers") {
            contentType(ContentType.Application.Json)
            setBody(Transfer(sender.id!!, recipient.id!!, transferMoneyAmount))
        }


        // then
        assertThat(response.status).describedAs("Transfer status").isEqualTo(HttpStatusCode.NoContent)

        val senderAfterTransfer = client.get("/accounts/${sender.id}").body<Account>()
        assertThat(senderAfterTransfer.moneyAmount).describedAs("Sender balance after transfer")
            .isEqualTo(senderMoneyAmount - transferMoneyAmount)

        val recipientAfterTransfer = client.get("/accounts/${recipient.id}").body<Account>()
        assertThat(recipientAfterTransfer.moneyAmount).describedAs("Recipient balance after transfer")
            .isEqualTo(recipientMoneyAmount + transferMoneyAmount)
    }

    @Test
    fun transferWithDirtyTrick() = testApplication {
        // config
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        // given
        val senderMoneyAmount = 79
        val recipientMoneyAmount = 33
        val transferMoneyAmount = 40

        val sender = client.post("/accounts") {
            contentType(ContentType.Application.Json)
            setBody(
                Account(
                    email = "alice@mail.com",
                    firstName = "Alice",
                    lastName = "N/A",
                    dateOfBirth = LocalDate(1991, 3, 17),
                    currency = "USD",
                    moneyAmount = senderMoneyAmount
                )
            )
        }.body<Account>()

        val recipient = client.post("/accounts") {
            contentType(ContentType.Application.Json)
            setBody(
                Account(
                    email = "bob@mail.com",
                    firstName = "Bob",
                    lastName = "N/A",
                    dateOfBirth = LocalDate(1991, 3, 17),
                    currency = "USD",
                    moneyAmount = recipientMoneyAmount
                )
            )
        }.body<Account>()


        // when
        val response = client.post("/accounts/transfers") {
            contentType(ContentType.Application.Json)
            setBody(Transfer(sender.id!!, recipient.id!!, transferMoneyAmount))
            header("Dirty-Trick-Header", "FAIL_TRANSFER")
        }


        // then
        assertThat(response.status).describedAs("Transfer status").isEqualTo(HttpStatusCode.InternalServerError)

        val senderAfterTransfer = client.get("/accounts/${sender.id}").body<Account>()
        assertThat(senderAfterTransfer).describedAs("Sender data after transfer")
            .usingRecursiveComparison()
            .isEqualTo(sender)

        val recipientAfterTransfer = client.get("/accounts/${recipient.id}").body<Account>()
        assertThat(recipientAfterTransfer).describedAs("Recipient data after transfer")
            .usingRecursiveComparison()
            .isEqualTo(recipient)
    }
}
