package com.example.quiz2_complaintapp.utils

object Constants {

    // Intent extras
    const val EXTRA_COMPLAINT_ID = "extra_complaint_id"

    // Complaint categories for the spinner
    val COMPLAINT_CATEGORIES = listOf(
        "Select Category",
        "IT",
        "Library",
        "Transport",
        "Hostel",
        "Accounts",
        "Examination",
        "Cafeteria",
        "Administration"
    )

    // Priority levels for the spinner
    val PRIORITY_LEVELS = listOf(
        "Select Priority",
        "Low",
        "Medium",
        "High",
        "Urgent"
    )

    // Status values
    const val STATUS_PENDING = "Pending"
    const val STATUS_IN_PROGRESS = "In Progress"
    const val STATUS_RESOLVED = "Resolved"

    // Priority colors (for use with resources — reference these in code)
    fun getPriorityColorRes(priority: String): Int {
        return when (priority) {
            "Low" -> android.R.color.holo_green_dark
            "Medium" -> android.R.color.holo_orange_light
            "High" -> android.R.color.holo_orange_dark
            "Urgent" -> android.R.color.holo_red_dark
            else -> android.R.color.darker_gray
        }
    }

    // Category icons (emoji for display simplicity)
    fun getCategoryEmoji(category: String): String {
        return when (category) {
            "IT" -> "💻"
            "Library" -> "📚"
            "Transport" -> "🚌"
            "Hostel" -> "🏠"
            "Accounts" -> "💰"
            "Examination" -> "📝"
            "Cafeteria" -> "🍽️"
            "Administration" -> "🏛️"
            else -> "📋"
        }
    }
}