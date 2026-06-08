package com.hophey.config


object JwtConfig {
    val jwtAudience = "jwt-audience"

    val jwtIssuer = "JetGo-issuer"

    val jwtRealm = "JetGoApp"

    val accessExpiringTime = 1L * 1000 * 60 * 60

    val refreshExpiringTime = 1L * 1000 * 60 * 60 * 24
}