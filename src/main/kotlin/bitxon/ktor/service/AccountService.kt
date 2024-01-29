package bitxon.ktor.service

import bitxon.ktor.api.model.Account
import bitxon.ktor.api.model.Transfer

interface AccountService {

    fun getAll(): List<Account>

    fun getById(id: Long): Account

    fun create(account: Account): Account

    fun transfer(transfer: Transfer, dirtyTrick: String?)
}