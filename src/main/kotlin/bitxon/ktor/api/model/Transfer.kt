package bitxon.ktor.api.model

import kotlinx.serialization.Serializable

@Serializable
data class Transfer(
    val senderId: Long,
    val recipientId: Long,
    val moneyAmount: Int
)