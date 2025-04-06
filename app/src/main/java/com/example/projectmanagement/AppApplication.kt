package com.example.projectmanagement

import android.app.Application
import com.example.projectmanagement.data.local.AppDatabase
import com.example.projectmanagement.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeDatabase()
    }
    private fun initializeDatabase() {
        val db = AppDatabase.getDatabase(applicationContext)
        val userDao = db.userDao()

        val applicationScope = CoroutineScope(SupervisorJob())

        applicationScope.launch {
            val existingUser = userDao.getUserByUsername("admin")
            if (existingUser == null) {
                userDao.insertUser(User(username = "admin", password = "123", email = "admin_itealab@gmail.com"))
            }
        }
    }
}