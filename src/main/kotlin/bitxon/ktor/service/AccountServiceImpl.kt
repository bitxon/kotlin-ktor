package bitxon.ktor.service

import bitxon.ktor.exception.ResourceNotFoundException
import bitxon.ktor.api.model.Account
import bitxon.ktor.db.AccountDao
import io.ktor.util.logging.*
import kotlin.reflect.jvm.jvmName

class AccountServiceImpl(
    private val accountDao: AccountDao
) : AccountService {
    private val log = KtorSimpleLogger(AccountServiceImpl::class.jvmName)

    override suspend fun getAll(): List<Account> {
        log.info("Getting all accounts")
        return accountDao.getAll()
    }

    override suspend fun getById(id: Long): Account {
        log.info("Getting one account by id: $id")
        return accountDao.getById(id) ?: throw ResourceNotFoundException("Account '$id' not found")
    }

    override suspend fun create(account: Account): Account {
        log.info("Creating account: ${account.email}")
        return accountDao.create(account) ?: throw IllegalStateException("Account not created")
    }
}