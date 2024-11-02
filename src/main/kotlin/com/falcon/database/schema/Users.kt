package com.falcon.database.schema

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.varchar

interface User : Entity<User> {
    companion object : Entity.Factory<User>()

    var userId: String
    var email: String
    var password: String
    var createdAt: String
}

object Users : Table<User>("Users") {
    val userId = varchar("user_id").primaryKey().bindTo { it.userId }
    val email = varchar("user_email").bindTo { it.email }
    val password = varchar("user_password").bindTo { it.password }
    val createdAt = varchar("created_at").bindTo { it.createdAt }
}
