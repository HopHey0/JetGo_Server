package com.hophey.service

import com.hophey.config.JwtConfig
import com.hophey.dto.AuthResponse
import com.hophey.repository.TokenRepository
import com.hophey.repository.UserRepository
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset


class AuthService(
    private val tokenRepository: TokenRepository,
    private val userRepository: UserRepository,
    private val jwtService: JwtService
) {

    fun register(email: String, password: String): AuthResponse?{
        if (userRepository.findByEmail(email) != null) { return null }

        val hashedPass = jwtService.getHashOfStr(password)
        val newUserId = userRepository.create(email, hashedPass)

        return createAuthResponse(email, newUserId)
    }

    fun login(email: String, password: String): AuthResponse?{
        val user = userRepository.findByEmail(email) ?: return null

        val hashedPass = jwtService.getHashOfStr(password)

        if (user.passwordHash != hashedPass) return null

        return createAuthResponse(email, user.id)
    }

    fun logout(clientToken: String): Boolean?{
        val clientTokenHash = jwtService.getHashOfStr(clientToken)
        val tokenDB = tokenRepository.findByTokenHash(clientTokenHash) ?: return null
        tokenRepository.deleteToken(tokenDB.id)
        return true
    }

    fun refresh(clientRefreshToken: String): AuthResponse?{
        val clientRefreshTokenHash = jwtService.getHashOfStr(clientRefreshToken)
        val refreshTokenDB = tokenRepository.findByTokenHash(clientRefreshTokenHash) ?: return null
        if (refreshTokenDB.expiresAt < OffsetDateTime.now(ZoneOffset.UTC)) return null
        val user = userRepository.findById(refreshTokenDB.userId) ?: return null

        val newRefreshToken = jwtService.generateRefreshToken(user.email)
        val newRefreshTokenHash = jwtService.getHashOfStr(newRefreshToken)
        tokenRepository.replaceToken(refreshTokenDB.id, newRefreshTokenHash, calculateExpiresAt())
        val accessToken = jwtService.generateAccessToken(user.email)
        return AuthResponse(
            newRefreshToken,
            accessToken,
        )
    }

    private fun createAuthResponse(email: String, userId: Int): AuthResponse{
        val refreshToken = jwtService.generateRefreshToken(email)
        val accessToken = jwtService.generateAccessToken(email)
        val refreshTokenHash = jwtService.getHashOfStr(refreshToken)

        tokenRepository.saveToken(userId, refreshTokenHash, calculateExpiresAt())
        return AuthResponse(refreshToken, accessToken)
    }

    private fun calculateExpiresAt(): OffsetDateTime = OffsetDateTime.ofInstant(
        Instant.ofEpochMilli(System.currentTimeMillis() + JwtConfig.refreshExpiringTime),
        ZoneOffset.UTC
    )
}