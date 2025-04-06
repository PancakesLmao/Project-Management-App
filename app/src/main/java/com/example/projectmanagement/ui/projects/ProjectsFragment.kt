package com.example.projectmanagement.ui.projects

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmanagement.CreateProjectActivity
import com.example.projectmanagement.databinding.FragmentProjectsBinding
import com.example.projectmanagement.viewmodel.ProjectsViewModel

class ProjectsFragment : Fragment() {

    private var _binding: FragmentProjectsBinding? = null
    private val binding get() = _binding!!
    private lateinit var projectsViewModel: ProjectsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        projectsViewModel = ViewModelProvider(this)[ProjectsViewModel::class.java]

        _binding = FragmentProjectsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recyclerViewProjects.layoutManager = LinearLayoutManager(context)
        projectsViewModel.projects.observe(viewLifecycleOwner) { projects ->
            binding.recyclerViewProjects.adapter = ProjectAdapter(projects)
        }

        binding.createProjectBtn.setOnClickListener {
//            Fragment instance is not a context, so we need to use requireContext()
            val intent = Intent(requireContext(), CreateProjectActivity::class.java)
            val userId = requireActivity().intent.getIntExtra("userId", 1)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        projectsViewModel.refreshProjects()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}