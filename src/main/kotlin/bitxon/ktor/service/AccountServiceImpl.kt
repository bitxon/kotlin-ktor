package bitxon.ktor.service

import bitxon.ktor.exception.ResourceNotFoundException
import bitxon.ktor.api.model.Account
import bitxon.ktor.api.model.Transfer
import bitxon.ktor.db.model.AccountEntity
import io.ktor.util.logging.*
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.reflect.jvm.jvmName

class AccountServiceImpl : AccountService {
    private val log = KtorSimpleLogger(AccountServiceImpl::class.jvmName)

    override fun getAll(): List<Account> = transaction {
        log.info("Getting all accounts")
        AccountEntity.all()
            .map { it.toApiAccount() }
    }

    override fun getById(id: Long): Account = transaction {
        log.info("Getting one account by id: $id")
        AccountEntity.findById(id)
            ?.toApiAccount()
            ?: throw ResourceNotFoundException("Account '$id' not found")
    }

    override fun create(account: Account): Account = transaction {
        log.info("Creating account: ${account.email}")
        AccountEntity.new { fromApiAccount(account) }
            .toApiAccount()
    }

    override fun transfer(transfer: Transfer, dirtyTrick: String?) = transaction {
        log.info("Transfer from: ${transfer.senderId} to: ${transfer.recipientId}")

        val sender = AccountEntity.findById(transfer.senderId)
            ?: throw ResourceNotFoundException("Sender not found")
        val recipient = AccountEntity.findById(transfer.recipientId)
            ?: throw ResourceNotFoundException("Recipient not found")

        sender.moneyAmount -= transfer.moneyAmount

        if ("FAIL_TRANSFER" == dirtyTrick) {
            throw IllegalStateException("Dirty trick")
        }

        recipient.moneyAmount += transfer.moneyAmount
    }

    private fun AccountEntity.fromApiAccount(source: Account) {
        email = source.email
        firstName = source.firstName
        lastName = source.lastName
        dateOfBirth = source.dateOfBirth.toJavaLocalDate()
        currency = source.currency
        moneyAmount = source.moneyAmount
    }

    private fun AccountEntity.toApiAccount() = Account(
        id = id.value,
        email = email,
        firstName = firstName,
        lastName = lastName,
        dateOfBirth = dateOfBirth.toKotlinLocalDate(),
        currency = currency,
        moneyAmount = moneyAmount
    )
}