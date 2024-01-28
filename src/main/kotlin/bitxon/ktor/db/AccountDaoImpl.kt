package bitxon.ktor.db

import bitxon.ktor.api.model.Account
import bitxon.ktor.db.model.AccountTable
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class AccountDaoImpl : AccountDao {

    override suspend fun getAll(): List<Account> = dbQuery {
        AccountTable.selectAll()
            .map(::mapRowToAccount)
    }

    override suspend fun getById(id: Long): Account? = dbQuery {
        AccountTable.selectAll()
            .where { AccountTable.id eq id }
            .map(::mapRowToAccount)
            .singleOrNull()
    }

    override suspend fun create(account: Account): Account? = dbQuery {
        AccountTable.insert {
            it[email] = account.email
            it[firstName] = account.firstName
            it[lastName] = account.lastName
            it[dateOfBirth] = account.dateOfBirth.toJavaLocalDate()
            it[currency] = account.currency
            it[moneyAmount] = account.moneyAmount
        }.resultedValues?.singleOrNull()?.let(::mapRowToAccount)
    }


    private suspend fun <T> dbQuery(block: suspend () -> T): T {
        return newSuspendedTransaction(Dispatchers.IO) { block() }
    }

    private fun mapRowToAccount(row: ResultRow) = Account(
        id = row[AccountTable.id].value,
        email = row[AccountTable.email],
        firstName = row[AccountTable.firstName],
        lastName = row[AccountTable.lastName],
        dateOfBirth = row[AccountTable.dateOfBirth].toKotlinLocalDate(),
        currency = row[AccountTable.currency],
        moneyAmount = row[AccountTable.moneyAmount]
    )
}
