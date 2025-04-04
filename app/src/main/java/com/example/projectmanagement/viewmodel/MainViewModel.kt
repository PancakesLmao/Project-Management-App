package com.example.projectmanagement.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _logoutResult = MutableLiveData<Boolean>()
    val logoutResult: LiveData<Boolean> get() = _logoutResult

    private val _username = MutableLiveData<String>()
    private val _email = MutableLiveData<String>()
    val username: LiveData<String> get() = _username
    val email: LiveData<String> get() = _email

    fun setUsername(username: String) {
        _username.postValue(username)
    }
    fun setEmail(email: String) {
        _email.postValue(email)
    }
    fun logout() {
//        Crashed
//        _logoutResult.value = true
//        Replace with postValue
        _logoutResult.postValue(true)
    }
}