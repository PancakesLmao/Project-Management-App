package com.example.projectmanagement.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData

class LoginViewModel: ViewModel() {
    private val _username = MutableLiveData<String>()
    private val _password = MutableLiveData<String>()

    val username: MutableLiveData<String> = _username
    val password: MutableLiveData<String> = _password

    fun login() {

    }
}