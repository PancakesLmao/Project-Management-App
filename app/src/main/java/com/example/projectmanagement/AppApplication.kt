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
        val db = AppDatabase.getDatabase(applicationContext) // Now applicationContext is available
        val userDao = db.userDao()

        // Create a coroutine scope tied to the application lifecycle
        val applicationScope = CoroutineScope(SupervisorJob())

        applicationScope.launch {
            // Now you can safely call the suspend function within the coroutine
            val existingUser = userDao.getUserByUsername("admin")
            if (existingUser == null) {
                userDao.insertUser(User(username = "admin", password = "123"))
            }
        }
    }
}