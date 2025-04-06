package com.example.projectmanagement.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.projectmanagement.data.local.AppDatabase
import com.example.projectmanagement.data.model.Project
import kotlinx.coroutines.launch

class EditViewModel(application: Application) : AndroidViewModel(application) {
    private val projectsDao = AppDatabase.getDatabase(application).projectsDao()
    private val _project = MutableLiveData<Project?>()
    val project: LiveData<Project?> get() = _project

    fun fetchProjectById(projectId: Int) {
        viewModelScope.launch {
            _project.postValue(projectsDao.getProjectById(projectId))
        }
    }
    fun updateProject(project: Project) {
        viewModelScope.launch {
            projectsDao.updateProject(project)
        }
    }
    fun deleteProject(project: Project) {
        viewModelScope.launch {
            projectsDao.deleteProject(project)
        }
    }
}