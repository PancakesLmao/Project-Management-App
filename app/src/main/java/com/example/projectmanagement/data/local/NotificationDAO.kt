package com.example.projectmanagement.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.projectmanagement.data.model.Notification

@Dao
interface NotificationDAO {
    @Insert
    suspend fun insertNotification(notification: Notification)

    @Query("SELECT * FROM notifications WHERE notificationId = :notificationId")
    suspend fun getNotificationById(notificationId: Int): Notification?

    @Query("SELECT * FROM notifications")
    suspend fun getAllNotifications(): List<Notification>
}