package com.falcon.service.db

import com.falcon.config.UserInfo

class DbService :IDbService{
    // mock db service layer just for testing
    override fun findUserByEmail(email: String): UserInfo {
        // simulate db call
        return UserInfo("john@gmail.com", "12345")
    }
}