package bitxon.ktor.db.model

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

object AccountTable : Table() {
    val id = long("id").autoIncrement()
    val email = varchar("email", 50)
    val firstName = varchar("first_name", 50)
    val lastName = varchar("last_name", 50)
    val dateOfBirth = date("date_of_birth")
    val currency = varchar("currency", 3)
    val moneyAmount = integer("money_amount")

    override val primaryKey = PrimaryKey(id)
}