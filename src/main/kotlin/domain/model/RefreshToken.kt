package com.hophey.domain.model

import java.time.OffsetDateTime

data class RefreshToken(
    val id: Long,
    val userId: Int,
    val refreshTokenHash: String,
    val expiresAt: OffsetDateTime,
    val createdAt: OffsetDateTime,
    val lastUsedAt: OffsetDateTime
)
