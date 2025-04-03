package com.example.projectmanagement.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.projectmanagement.AppApplication

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _logoutResult = MutableLiveData<Boolean>()
    val logoutResult: LiveData<Boolean> get() = _logoutResult

    fun logout() {
//        Crashed
//        _logoutResult.value = true
//        Replace with postValue
        _logoutResult.postValue(true)
    }
}