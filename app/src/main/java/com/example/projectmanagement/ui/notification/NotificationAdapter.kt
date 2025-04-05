package com.example.projectmanagement.ui.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanagement.data.model.Notification
import com.example.projectmanagement.databinding.ItemNotificationBinding

class NotificationAdapter(private val notifications: List<Notification>) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: Notification) {
            binding.notificationHeading.text = notification.heading
            binding.notificationContent.text = notification.content
            binding.notificationDate.text = "Due date: ${notification.eventDueDate}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, index: Int) {
        holder.bind(notifications[index])
    }

    override fun getItemCount() = notifications.size
}