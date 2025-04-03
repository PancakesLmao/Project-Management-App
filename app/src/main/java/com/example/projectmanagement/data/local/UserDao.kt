package com.example.projectmanagement.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.projectmanagement.data.model.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user_accounts WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?
}