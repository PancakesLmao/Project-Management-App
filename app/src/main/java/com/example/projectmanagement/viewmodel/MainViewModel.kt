package com.example.projectmanagement.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.projectmanagement.AppApplication

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _logoutResult = MutableLiveData<Boolean>()
    val logoutResult: LiveData<Boolean> get() = _logoutResult

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> get() = _username

    fun setUsername(username: String) {
        _username.postValue(username)
    }
    fun logout() {
//        Crashed
//        _logoutResult.value = true
//        Replace with postValue
        _logoutResult.postValue(true)
    }
}