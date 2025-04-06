package com.example.projectmanagement

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.projectmanagement.databinding.ActivityLoginBinding
import com.example.projectmanagement.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()
            loginViewModel.login(username, password)
        }
        //        Observe the loginResult LiveData from the ViewModel
        loginViewModel.loginResult.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Successfully login", Toast.LENGTH_SHORT).show()

                // Observe both email and userId before start MainActivity
                loginViewModel.userEmail.observe(this) { email ->
                    loginViewModel.userId.observe(this) { userId ->
                        if (!email.isNullOrEmpty() && userId != null) {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("username", binding.username.text.toString())
                            intent.putExtra("email", email)
                            intent.putExtra("userId", userId)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "User information not found", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
