package bitxon.ktor.service

import bitxon.ktor.exception.ResourceNotFoundException
import bitxon.ktor.api.model.Account
import bitxon.ktor.api.model.Transfer
import bitxon.ktor.db.AccountDao
import io.ktor.util.logging.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
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

    override suspend fun transfer(transfer: Transfer, dirtyTrick: String?) {
        log.info("Transfer from: ${transfer.senderId} to: ${transfer.recipientId}")

        newSuspendedTransaction {
            val sender = accountDao.getById(transfer.senderId)
                ?: throw ResourceNotFoundException("Sender not found")
            val recipient = accountDao.getById(transfer.recipientId)
                ?: throw ResourceNotFoundException("Recipient not found")

            val senderToUpdate = sender.copy(moneyAmount = sender.moneyAmount - transfer.moneyAmount)
            accountDao.update(senderToUpdate)

            if ("FAIL_TRANSFER" == dirtyTrick) {
                throw IllegalStateException("Dirty trick")
            }

            val recipientToUpdate = recipient.copy(moneyAmount = recipient.moneyAmount + transfer.moneyAmount)
            accountDao.update(recipientToUpdate)
        }
    }
}