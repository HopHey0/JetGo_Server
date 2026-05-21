package com.hophey.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.sql.Date


internal object JwtConfig {
    val jwtAudience = "jwt-audience"

    val jwtDomain = "https://jwt-provider-domain/"

    val jwtRealm = "JetGoApp"

    val jwtSecret = "secret"

    val expiringTime = 1L * 1000 * 60 * 60 * 24

    fun generateToken(username: String, isSuccess: Boolean): String {
        return JWT.create()
            .withAudience(jwtAudience)
            .withIssuer(jwtRealm)
            .withClaim("username", username)
            .withClaim("isSuccess", isSuccess)
            .withExpiresAt(Date(System.currentTimeMillis() + expiringTime))
            .sign(Algorithm.HMAC256(jwtSecret))
    }
}