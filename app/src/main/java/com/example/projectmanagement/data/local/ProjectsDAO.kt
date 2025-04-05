package com.example.projectmanagement.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.projectmanagement.data.model.Project

@Dao
interface ProjectsDAO {
    @Insert
    suspend fun insertProject(project: Project)

    @Query("SELECT * FROM projects WHERE projectId = :projectId")
    suspend fun getProjectById(projectId: Int): Project?

    @Query("SELECT * FROM projects")
    suspend fun getAllProjects(): List<Project>

//    @Query("SELECT * FROM projects LIMIT 4")
//    suspend fun getAllProjectsLimited(): List<Project>
}