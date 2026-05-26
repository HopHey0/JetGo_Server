package com.hophey.repository.tables

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone

object UserTokens : Table("user_tokens") {
    val id = long("user_token_id").autoIncrement()
    val userId = integer("user_id").references(Users.id)
    val refreshTokenHash = varchar("refresh_token_hash", 255)
    val expiresAt = timestampWithTimeZone("expires_at")
    val createdAt = timestampWithTimeZone("created_at")
    val lastUsedAt = timestampWithTimeZone("last_used_at")

    override val primaryKey = PrimaryKey(id)
}