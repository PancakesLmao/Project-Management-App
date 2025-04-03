package com.example.projectmanagement.data.repository

import com.example.projectmanagement.data.local.UserDao
import com.example.projectmanagement.data.model.User

class UserRepository(private val userDao: UserDao) {

    suspend fun getUserByUsername(username: String): User? {
        return userDao.getUserByUsername(username)
    }
}
