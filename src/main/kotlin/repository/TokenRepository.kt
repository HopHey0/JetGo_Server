package com.hophey.repository

import com.hophey.domain.model.RefreshToken
import com.hophey.repository.tables.UserTokens
import com.hophey.repository.tables.Users
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import java.time.OffsetDateTime
import java.time.ZoneOffset

class TokenRepository() {

    fun saveToken(userId: Int, refreshTokenHash: String, expiresAt: OffsetDateTime) = transaction {
        UserTokens.insert {
            it[this.userId] = userId
            it[this.refreshTokenHash] = refreshTokenHash
            it[this.expiresAt] = expiresAt
            it[createdAt] = OffsetDateTime.now(ZoneOffset.UTC)
        }[UserTokens.id]
    }

    fun findByTokenHash(refreshTokenHash: String): RefreshToken? = transaction {
        UserTokens.selectAll().where { UserTokens.refreshTokenHash eq refreshTokenHash }
            .map { row -> rowToToken(row) }
            .singleOrNull()
    }

    fun findByUserId(userId: Int) = transaction {
        UserTokens.selectAll().where { UserTokens.userId eq userId }
            .map { row -> rowToToken(row) }
            .singleOrNull()
    }

    fun replaceToken(tokenId: Long, newRefreshTokenHash: String, newExpiresAt: OffsetDateTime) = transaction {
        UserTokens.update({ UserTokens.id eq tokenId }) {
            it[this.refreshTokenHash] = newRefreshTokenHash
            it[this.expiresAt] = newExpiresAt
            it[this.lastUsedAt] = OffsetDateTime.now(ZoneOffset.UTC)
        }
    }

    fun deleteToken(tokenId: Long) = transaction {
        UserTokens.deleteWhere { UserTokens.id eq tokenId }
    }

    private fun rowToToken(row: ResultRow): RefreshToken {
        return RefreshToken(
            id = row[UserTokens.id],
            userId = row[UserTokens.userId],
            refreshTokenHash = row[UserTokens.refreshTokenHash],
            expiresAt = row[UserTokens.expiresAt],
            createdAt = row[UserTokens.createdAt],
            lastUsedAt = row[UserTokens.lastUsedAt],
        )
    }
}