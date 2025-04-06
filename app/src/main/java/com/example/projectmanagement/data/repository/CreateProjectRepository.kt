package com.example.projectmanagement.data.repository

import com.example.projectmanagement.data.local.NotificationDAO
import com.example.projectmanagement.data.local.ProjectsDAO
import com.example.projectmanagement.data.model.Notification
import com.example.projectmanagement.data.model.Project

class CreateProjectRepository(private val projectsDAO: ProjectsDAO, private val notificationDAO: NotificationDAO) {
    suspend fun insertNewProject(project: Project): Long {
        val projectId = projectsDAO.insertNewProject(project)

        val notification = Notification(
            heading = "Project Due Date",
            content = "Don't forget your deadline for ${project.name}",
            fromProject = projectId.toInt(),
            eventDueDate = project.dueDate
        )

        notificationDAO.insertNotification(notification)

        return projectId
    }
}