package com.example.projectmanagement

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.projectmanagement.data.model.Project
import com.example.projectmanagement.databinding.ActivityEditBinding
import com.example.projectmanagement.viewmodel.EditViewModel

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    private val editViewModel: EditViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Spinner setup
        val statusList = listOf("In Progress", "Completed", "Reviewing")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statusList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerStatus.adapter = adapter

        val projectId = intent.getIntExtra("PROJECT_ID", -1)
        if (projectId != -1) {
            editViewModel.fetchProjectById(projectId)
            Log.d("EditActivity", "Project ID: $projectId")
        } else {
            Log.e("EditActivity", "Invalid project ID")
        }
        editViewModel.project.observe(this) { project ->
            project?.let {
                binding.editProjectName.setText(it.name)
                binding.editProjectDesc.setText(it.description)
                binding.editStartDate.setText(it.startDate)
                binding.editEndDate.setText(it.dueDate)

                val statusPosition = statusList.indexOf(it.status)
                if (statusPosition != -1) {
                    binding.spinnerStatus.setSelection(statusPosition)
                }
            }
        }

        binding.updateBtn.setOnClickListener {
            val updatedProject = Project(
                projectId = projectId,
                name = binding.editProjectName.text.toString(),
                description = binding.editProjectDesc.text.toString(),
                startDate = binding.editStartDate.text.toString(),
                dueDate = binding.editEndDate.text.toString(),
                status = binding.spinnerStatus.selectedItem.toString(),
                createdBy = intent.getIntExtra("userId", 1)
            )
            editViewModel.updateProject(updatedProject)
            finish()
        }

        binding.deleteBtn.setOnClickListener {
            val projectToDelete = Project(
                projectId = projectId,
                name = "",
                description = "",
                startDate = "",
                dueDate = "",
                status = "",
                createdBy = 1
            )
            editViewModel.deleteProject(projectToDelete)
            finish()
        }

        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}