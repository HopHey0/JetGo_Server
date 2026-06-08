package com.hophey.repository.database

import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.ApplicationConfig
import org.jetbrains.exposed.v1.jdbc.Database

object DatabaseFactory {
    fun init(config: ApplicationConfig) {

        val hikariConfig = com.zaxxer.hikari.HikariConfig().apply {
            jdbcUrl = config.property("ktor.db.jdbcUrl").getString()
            driverClassName = "org.postgresql.Driver"
            username = config.property("ktor.db.username").getString()
            password = config.property("ktor.db.password").getString()
            maximumPoolSize = 10
            minimumIdle = 5
            idleTimeout = 300000
            connectionTimeout = 30000
        }
        val dataSource: HikariDataSource = HikariDataSource(hikariConfig)

        Database.connect(dataSource)
    }
}