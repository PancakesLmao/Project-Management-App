package com.example.projectmanagement.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.projectmanagement.data.local.AppDatabase
import com.example.projectmanagement.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository
    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> get() = _loginResult

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val user = repository.getUserByUsername(username)
            if (user != null) {
                if (user.password == password) {
                    Log.d("LoginViewModel", "Login successful for user: $username")
                    _loginResult.value = true
                } else {
                    Log.e("LoginViewModel", "Incorrect password for user: $username")
                    _loginResult.value = false
                }
            } else {
                Log.e("LoginViewModel", "User not found")
                _loginResult.value = false
            }
        }
    }
}
