package com.falcon.service.db

import com.falcon.config.UserInfo
import com.falcon.database.DatabaseClient
import com.falcon.database.schema.User
import com.falcon.database.schema.Users
import com.falcon.utils.Logger
import org.ktorm.dsl.eq
import org.ktorm.dsl.insert
import org.ktorm.entity.find
import org.ktorm.entity.sequenceOf
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class DbService : IDbService {
    private val logger = Logger.getLogger(DbService::class.java)

    override suspend fun findUserByEmail(email: String): User? {
        return DatabaseClient.database.sequenceOf(Users).find { it.email eq email }
    }

    override suspend fun addNewUser(newUser: UserInfo) {
        try {
            val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

            DatabaseClient.database.insert(Users) {
                set(it.userId, UUID.randomUUID().toString())
                set(it.email, newUser.email)
                set(it.password, newUser.password)
                set(it.createdAt, currentDateTime)
            }
            logger.info("User Added Successfully")
        } catch (e: Exception) {
            logger.error("Failed to Add User", e)
            throw e
        }
    }
}
