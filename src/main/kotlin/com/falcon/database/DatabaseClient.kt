package com.falcon.database

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import org.ktorm.database.Database

object DatabaseClient {
    private val config: Config = ConfigFactory.load()
    private val dbUrl = config.getString("falcon.mysql-database.url")
    private val dbUser = config.getString("falcon.mysql-database.user")
    private val dbPassword = config.getString("falcon.mysql-database.password")

    fun initDB(): Database {
        val connection: Database =
            Database.connect(
                url = dbUrl,
                user = dbUser,
                password = dbPassword,
            )
        return connection
    }
}
