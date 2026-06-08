package com.hophey.dto

import com.hophey.domain.model.RefreshToken
import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val username: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val refreshToken: String,
    val accessToken: String,
)

@Serializable
data class LogoutRequest(
    val refreshToken: String
)

@Serializable
data class RefreshTokenRequest(
    val refreshToken: String
)