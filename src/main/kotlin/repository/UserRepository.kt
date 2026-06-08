package com.hophey.repository

import com.hophey.domain.model.User
import com.hophey.repository.tables.authTables.Users
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class UserRepository {
    fun findById(id: Int): User? = transaction {
        Users.selectAll().where { Users.id eq id }
            .map { rowToUser(it) }
            .singleOrNull()
    }

    fun findByEmail(email: String) = transaction {
        Users.selectAll().where { Users.email eq email }
            .map { rowToUser(it) }
            .singleOrNull()
    }

    fun create(email: String, passwordHash: String): Int = transaction {
        Users.insert {
            it[this.email] = email
            it[this.passwordHash] = passwordHash
        }[Users.id]
    }

    fun delete(id: Int): Boolean = transaction {
        Users.deleteWhere { Users.id eq id } > 0
    }

    private fun rowToUser(row: ResultRow) = User(
        id = row[Users.id],
        email = row[Users.email],
        passwordHash = row[Users.passwordHash],
    )
}