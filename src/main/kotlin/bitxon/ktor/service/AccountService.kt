package bitxon.ktor.service

import bitxon.ktor.api.model.Account
import bitxon.ktor.api.model.Transfer

interface AccountService {

    suspend fun getAll(): List<Account>

    suspend fun getById(id: Long): Account

    suspend fun create(account: Account): Account

    suspend fun transfer(transfer: Transfer, dirtyTrick: String?)
}