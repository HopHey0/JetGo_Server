package com.hophey.repository.database

import com.hophey.config.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.v1.jdbc.Database

object DatabaseFactory {
    fun init() {
        val dataSource: HikariDataSource = HikariDataSource(HikariConfig.config)
        Database.connect(dataSource)
    }
}