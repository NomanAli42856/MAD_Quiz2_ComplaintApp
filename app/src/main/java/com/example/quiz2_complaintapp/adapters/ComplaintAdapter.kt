package com.example.quiz2_complaintapp.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quiz2_complaintapp.databinding.ItemComplaintBinding
import com.example.quiz2_complaintapp.models.Complaint
import com.example.quiz2_complaintapp.utils.Constants

class ComplaintAdapter(
    private val onItemClick: (Complaint) -> Unit,
    private val onEditClick: (Complaint) -> Unit,
    private val onDeleteClick: (Complaint) -> Unit
) : ListAdapter<Complaint, ComplaintAdapter.ComplaintViewHolder>(ComplaintDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintViewHolder {
        val binding = ItemComplaintBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ComplaintViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ComplaintViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ComplaintViewHolder(
        private val binding: ItemComplaintBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(complaint: Complaint) {
            binding.apply {
                tvComplaintTitle.text = complaint.complaintTitle
                tvStudentName.text = "👤 ${complaint.studentName}"
                tvRollNumber.text = complaint.rollNumber
                tvCategory.text = "${Constants.getCategoryEmoji(complaint.category)}  ${complaint.category}"
                tvPriorityBadge.text = complaint.priority
                tvStatus.text = complaint.status

                // Priority strip + badge color
                val priorityColor = when (complaint.priority) {
                    "Low"    -> Color.parseColor("#388E3C")
                    "Medium" -> Color.parseColor("#F57C00")
                    "High"   -> Color.parseColor("#E64A19")
                    "Urgent" -> Color.parseColor("#C62828")
                    else     -> Color.parseColor("#1565C0")
                }
                priorityStrip.setBackgroundColor(priorityColor)
                tvPriorityBadge.backgroundTintList = ColorStateList.valueOf(priorityColor)

                // Status badge color
                val statusColor = when (complaint.status) {
                    "Pending"     -> Color.parseColor("#F57C00")
                    "In Progress" -> Color.parseColor("#1565C0")
                    "Resolved"    -> Color.parseColor("#388E3C")
                    else          -> Color.parseColor("#888888")
                }
                tvStatus.backgroundTintList = ColorStateList.valueOf(statusColor)

                // Click listeners
                root.setOnClickListener { onItemClick(complaint) }
                btnEdit.setOnClickListener { onEditClick(complaint) }
                btnDelete.setOnClickListener { onDeleteClick(complaint) }
            }
        }
    }

    class ComplaintDiffCallback : DiffUtil.ItemCallback<Complaint>() {
        override fun areItemsTheSame(oldItem: Complaint, newItem: Complaint) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Complaint, newItem: Complaint) = oldItem == newItem
    }
}