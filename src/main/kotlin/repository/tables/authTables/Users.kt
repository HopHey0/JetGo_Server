package com.hophey.repository.tables.authTables

import org.jetbrains.exposed.v1.core.Table

object Users : Table("users") {
    val id = integer("user_id").autoIncrement()
    val email = varchar("email", 50).uniqueIndex()
    val passwordHash = varchar("password_hash", 255)

    override val primaryKey = PrimaryKey(id)
}