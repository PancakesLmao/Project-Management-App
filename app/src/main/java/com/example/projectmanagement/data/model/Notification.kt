package com.example.projectmanagement.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "notifications",
    foreignKeys = [ForeignKey(
        entity = Project::class,
        parentColumns = ["projectId"],
        childColumns = ["fromProject"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )],
    indices = [Index("fromProject")]
)
data class Notification(
    @PrimaryKey(autoGenerate = true) val notificationId: Int = 0,
    val heading: String,
    val content: String,
    val fromProject: Int,
    val eventDueDate: String
)