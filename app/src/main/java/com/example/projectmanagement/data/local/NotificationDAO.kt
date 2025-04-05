package com.example.projectmanagement.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.projectmanagement.data.model.Notification

@Dao
interface NotificationDAO {
    @Insert
    suspend fun insertNotification(notification: Notification) : Long

    @Query("SELECT * FROM notifications WHERE notificationId = :notificationId")
    suspend fun getNotificationById(notificationId: Int): Notification?

//    @Query("SELECT * FROM notifications")
//    suspend fun getAllNotifications(): List<Notification>

    @Query("""
    SELECT 
        n.notificationId, 
        n.heading, 
        n.content, 
        n.fromProject, 
        p.dueDate AS eventDueDate
    FROM notifications n
    JOIN projects p ON n.fromProject = p.projectId
""")
    suspend fun getAllNotifications(): List<Notification>

}