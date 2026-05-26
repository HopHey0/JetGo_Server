package com.hophey.service

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.hophey.config.JwtConfig
import com.hophey.config.JwtConfig.jwtAudience
import com.hophey.config.JwtConfig.jwtRealm
import com.hophey.config.JwtConfig.jwtSecret
import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.auth.jwt.JWTPrincipal
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.sql.Date

class JwtService() {

    companion object {
        val verifier: JWTVerifier = JWT.require(Algorithm.HMAC256(JwtConfig.jwtSecret))
        .withAudience(JwtConfig.jwtAudience)
        .withIssuer(JwtConfig.jwtRealm)
        .build()
    }

    fun validator(credential: JWTCredential): JWTPrincipal?{
        val username = credential.payload.getClaim("username").asString()
        val exp = credential.payload.expiresAt?.time ?: 0
        val now = System.currentTimeMillis()
        val isExpired = exp < now

        return if (username != null && !isExpired){
            JWTPrincipal(payload = credential.payload)
        } else {
            null
        }
    }

    fun generateAccessToken(username: String): String{
        return generateToken(username, JwtConfig.accessExpiringTime)
    }

    fun generateRefreshToken(username: String): String{
        return generateToken(username, JwtConfig.refreshExpiringTime)
    }

    fun getHashOfStr(input: String): String{
        val bytes = input.toByteArray(StandardCharsets.UTF_8)
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(bytes)
        return hashBytes.joinToString("") { "%02x".format(it) }
    }


    private fun generateToken(username: String, expiringTime: Long): String {
        return JWT.create()
            .withAudience(jwtAudience)
            .withIssuer(jwtRealm)
            .withClaim("username", username)
            .withExpiresAt(Date(System.currentTimeMillis() + expiringTime))
            .sign(Algorithm.HMAC256(jwtSecret))
    }
}