package bitxon.ktor.api.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val id: Long? = null,
    val email: String,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: LocalDate,
    val currency: String,
    val moneyAmount: Int
)
