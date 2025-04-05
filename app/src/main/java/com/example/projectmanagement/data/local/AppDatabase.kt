package com.example.projectmanagement.data.local

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.projectmanagement.data.model.Notification
import com.example.projectmanagement.data.model.Project
import com.example.projectmanagement.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [User::class, Project::class, Notification::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun projectsDao(): ProjectsDAO
    abstract fun notificationDao() : NotificationDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "project_management_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(database)
                }
            }
        }

        private suspend fun populateDatabase(database: AppDatabase) {
            // First create a user
            val userDao = database.userDao()
            val userId = userDao.insertUser(
                User(
                    username = "admin",
                    password = "123",
                    email = "admin@example.com"
                )
            )

            Log.d("AppDatabase", "Created default user with ID: $userId")

            val projectsDao = database.projectsDao()
            val notificationDao = database.notificationDao()
            val projects = listOf(
                Project(
                    name = "Android Development",
                    description = "Create a project management app",
                    startDate = "2023-05-01",
                    dueDate = "2023-08-31",
                    status = "In Progress",
                    createdBy = userId.toInt()
                ),
                Project(
                    name = "Database Design",
                    description = "Design a Room database for the app",
                    startDate = "2023-04-15",
                    dueDate = "2023-05-15",
                    status = "Completed",
                    createdBy = userId.toInt()
                ),
                Project(
                    name = "UI Implementation",
                    description = "Implement the app's user interface",
                    startDate = "2023-06-01",
                    dueDate = "2023-07-15",
                    status = "In Progress",
                    createdBy = userId.toInt()
                )
            )

            projects.forEach { project ->
                val projectId = projectsDao.insertProject(project)

                val notification = Notification(
                    heading = "Project Due Date",
                    content = "Don't forget your deadline for ${project.name}",
                    fromProject = projectId.toInt()
                )

                notificationDao.insertNotification(notification)
            }

            Log.d("AppDatabase", "Created ${projects.size} sample projects")
        }
    }
}