package com.hophey.config

import com.zaxxer.hikari.HikariConfig

object HikariConfig {
    val config = HikariConfig().apply {
        jdbcUrl = "127.0.0.1:5432"
        driverClassName = "org.postgresql.Driver"
        username = "postgres"
        password = "postgres"
        maximumPoolSize = 10
        minimumIdle = 5
        idleTimeout = 300000
        connectionTimeout = 30000
    }
}