package com.example.projectmanagement.ui.projects

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanagement.data.model.Project
import com.example.projectmanagement.databinding.ItemProjectBinding

class ProjectAdapter(private val projects: List<Project>) : RecyclerView.Adapter<ProjectAdapter.ViewHolder>() {

    class ViewHolder(internal val binding: ItemProjectBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(project: Project) {
            binding.projectName.text = project.name
            binding.projectDescription.text = project.description
            binding.projectDueDate.text = "Due date: ${project.dueDate}"
            binding.projectStatus.text = "Status: ${project.status}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, index: Int) {
        holder.bind(projects[index])

        holder.binding.editProjectBtn.setOnClickListener {
            val intent = android.content.Intent(holder.itemView.context, com.example.projectmanagement.EditActivity::class.java).apply {
                putExtra("project_id", projects[index].projectId)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = projects.size
}