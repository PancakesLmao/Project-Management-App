package com.example.projectmanagement.ui.projects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmanagement.databinding.FragmentProjectsBinding
import com.example.projectmanagement.viewmodel.ProjectsViewModel

class ProjectsFragment : Fragment() {

    private var _binding: FragmentProjectsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val projectsViewModel =
            ViewModelProvider(this).get(ProjectsViewModel::class.java)

        _binding = FragmentProjectsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up RecyclerView
        binding.recyclerViewProjects.layoutManager = LinearLayoutManager(context)

        // Observe projects data
        projectsViewModel.projects.observe(viewLifecycleOwner) { projects ->
            binding.recyclerViewProjects.adapter = ProjectAdapter(projects)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}