package com.example.projectmanagement.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.projectmanagement.data.local.AppDatabase
import com.example.projectmanagement.data.model.Project
import kotlinx.coroutines.launch

class ProjectsViewModel(application: Application) : AndroidViewModel(application) {

    private val projectsDAO = AppDatabase.getDatabase(application).projectsDao()

    private val _projects = MutableLiveData<List<Project>>()
    val projects: LiveData<List<Project>> = _projects

    init {
        loadProjects()
    }

    private fun loadProjects() {
        viewModelScope.launch {
            try {
                // Assuming you'll add this function to ProjectsDAO
                val projectList = projectsDAO.getAllProjects()
                _projects.postValue(projectList)
            } catch (e: Exception) {
                // Handle error
                _projects.postValue(emptyList())
            }
        }
    }
}