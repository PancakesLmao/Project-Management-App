package com.example.projectmanagement.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.projectmanagement.data.local.AppDatabase
import com.example.projectmanagement.data.model.Notification
import kotlinx.coroutines.launch

class NotificationViewModel(application: Application) : AndroidViewModel(application) {

    private val notificationDAO = AppDatabase.getDatabase(application).notificationDao()
    private val _notifications = MutableLiveData<List<Notification>>()
    val notifications: LiveData<List<Notification>> = _notifications

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            try {
                val notificationList = notificationDAO.getAllNotifications()
                _notifications.postValue(notificationList)
            } catch (e: Exception) {
                _notifications.postValue(emptyList())
            }
        }
    }
}