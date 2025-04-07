package com.example.projectmanagement

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.projectmanagement.databinding.ActivityCreateProjectBinding
import com.example.projectmanagement.viewmodel.CreateProjectViewModel
import kotlin.getValue

class CreateProjectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateProjectBinding
    private val viewModel: CreateProjectViewModel by viewModels()
    private var userId: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getIntExtra("userId", 1)

        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.createProjectBtn.setOnClickListener {
            createNewProject()
        }

        viewModel.createResult.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Project created successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to create project", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.createdProject.observe(this) { project ->
            Log.d("CreateProjectActivity", "New project created: ${project.projectId}, Name: ${project.name}, " +
                    "Description: ${project.description}, Start Date: ${project.startDate}, " +
                    "Due Date: ${project.dueDate}, Status: ${project.status}, Created By: ${project.createdBy}")
        }
    }

    private fun createNewProject() {
        val name = binding.editProjectName.text.toString().trim()
        val description = binding.etProjectDescription.text.toString().trim()
        val startDate = binding.editStartDate.text.toString().trim()
        val dueDate = binding.editEndDate.text.toString().trim()

        if (name.isEmpty()) {
            binding.editProjectName.error = "Project name is required"
            return
        }

        if (startDate.isEmpty()) {
            binding.editStartDate.error = "Start date is required"
            return
        }

        if (dueDate.isEmpty()) {
            binding.editEndDate.error = "Due date is required"
            return
        }
        val createdBy = userId

        viewModel.createNewProject(name, description, startDate, dueDate, createdBy)
    }
}