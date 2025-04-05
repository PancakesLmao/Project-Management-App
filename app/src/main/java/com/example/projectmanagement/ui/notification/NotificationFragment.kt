package com.example.projectmanagement.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmanagement.databinding.FragmentNotificationBinding
import com.example.projectmanagement.viewmodel.NotificationViewModel

class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationViewModel =
            ViewModelProvider(this).get(NotificationViewModel::class.java)

        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up RecyclerView
        binding.recyclerViewNotifications.layoutManager = LinearLayoutManager(context)
        // Observe notifications data
        notificationViewModel.notifications.observe(viewLifecycleOwner) { notifications ->
            binding.recyclerViewNotifications.adapter = NotificationAdapter(notifications)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}