// models/Complaint.kt
package com.example.quiz2_complaintapp.models

data class Complaint(
    val id: String = "",
    val studentName: String = "",
    val rollNumber: String = "",
    val complaintTitle: String = "",
    val category: String = "",
    val priority: String = "",
    val description: String = "",
    val status: String = "Pending",
    val createdAt: Long = System.currentTimeMillis()  // simple timestamp, no annotation needed
)