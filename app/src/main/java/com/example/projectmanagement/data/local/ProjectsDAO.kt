package com.example.projectmanagement.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.projectmanagement.data.model.Project

@Dao
interface ProjectsDAO {
    @Insert
    suspend fun insertProject(project: Project) : Long

    @Query("SELECT * FROM projects WHERE projectId = :projectId")
    suspend fun getProjectById(projectId: Int): Project?

    @Query("SELECT * FROM projects")
    suspend fun getAllProjects(): List<Project>

//    @Query("SELECT * FROM projects LIMIT 4")
//    suspend fun getAllProjectsLimited(): List<Project>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewProject(project: Project): Long

    @Update
    suspend fun updateProject(project: Project)

    @Delete
    suspend fun deleteProject(project: Project)
}