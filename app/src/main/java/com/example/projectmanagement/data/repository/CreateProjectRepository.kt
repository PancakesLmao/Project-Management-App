package com.example.projectmanagement.data.repository

import com.example.projectmanagement.data.local.ProjectsDAO
import com.example.projectmanagement.data.model.Project

class CreateProjectRepository(private val projectsDAO: ProjectsDAO) {
    suspend fun insertNewProject(project: Project): Long {
        return projectsDAO.insertNewProject(project)
    }
}