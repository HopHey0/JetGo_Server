package com.hophey.domain.usecase

import com.hophey.config.JwtConfig
import kotlin.random.Random

class LoginUseCase(

) {
    suspend fun login(username: String, password: String): String?{
        if (Random.nextBoolean()){
            return null
        }
        return JwtConfig.generateToken(username, true)
    }
}