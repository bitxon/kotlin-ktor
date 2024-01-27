package bitxon.ktor.service

import bitxon.ktor.api.model.Account

interface AccountService {

    suspend fun getAll(): List<Account>

    suspend fun getById(id: Long): Account

    suspend fun create(account: Account): Account
}