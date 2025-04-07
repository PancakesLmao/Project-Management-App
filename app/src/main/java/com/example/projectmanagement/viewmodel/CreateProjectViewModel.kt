package com.example.projectmanagement.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.projectmanagement.data.local.AppDatabase
import com.example.projectmanagement.data.model.Project
import com.example.projectmanagement.data.repository.CreateProjectRepository
import kotlinx.coroutines.launch
import kotlin.compareTo
import kotlin.text.toInt

class CreateProjectViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CreateProjectRepository
    private val _createResult = MutableLiveData<Boolean>()
    val createResult: LiveData<Boolean> get() = _createResult

    private val _createdProject = MutableLiveData<Project>()
    val createdProject: LiveData<Project> get() = _createdProject

    init {
        val projectsDao = AppDatabase.getDatabase(application).projectsDao()
        val notificationDao = AppDatabase.getDatabase(application).notificationDao()
        repository = CreateProjectRepository(projectsDao, notificationDao)
    }

    fun createNewProject(name: String, description: String, startDate: String, dueDate: String, createdBy: Int) {
        viewModelScope.launch {
            val project = Project(
//                projectId = 0, // Room will auto-generate the ID
                name = name,
                description = description,
                startDate = startDate,
                dueDate = dueDate,
                status = "In progress",
                createdBy = createdBy
            )
            val result = repository.insertNewProject(project)
            _createResult.postValue(result > 0)
            if (result > 0) {
                _createdProject.postValue(project.copy(projectId = result.toInt()))
            }
        }
    }
}