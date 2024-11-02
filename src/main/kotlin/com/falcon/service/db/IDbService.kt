package com.falcon.service.db

import com.falcon.config.UserInfo
import com.falcon.database.schema.User

interface IDbService {
    suspend fun findUserByEmail(email: String): User?

    suspend fun addNewUser(newUser: UserInfo)
}
