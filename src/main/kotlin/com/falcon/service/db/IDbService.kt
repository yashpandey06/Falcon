package com.falcon.service.db

import com.falcon.config.UserInfo

interface IDbService {
    fun findUserByEmail(email: String): UserInfo?
}