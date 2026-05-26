package com.hophey.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.sql.Date


object JwtConfig {
    val jwtAudience = "jwt-audience"

    val jwtDomain = "https://jwt-provider-domain/"

    val jwtRealm = "JetGoApp"

    val jwtSecret = "secret"

    val accessExpiringTime = 1L * 1000 * 60 * 60

    val refreshExpiringTime = 1L * 1000 * 60 * 60 * 24
}