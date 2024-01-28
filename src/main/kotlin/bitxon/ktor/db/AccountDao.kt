package bitxon.ktor.db

import bitxon.ktor.api.model.Account

interface AccountDao {

    suspend fun getAll(): List<Account>

    suspend fun getById(id: Long): Account?

    suspend fun create(account: Account): Account?

    suspend fun update(account: Account): Int
}