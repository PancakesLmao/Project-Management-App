package com.example.projectmanagement.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "projects",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["createdBy"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )],
    indices = [Index("createdBy")]
)
data class Project(
    @PrimaryKey(autoGenerate = true) val projectId: Int = 0,
    val name: String,
    val description: String,
    val startDate: String,
    val dueDate: String,
    val status: String,
    val createdBy: Int
)