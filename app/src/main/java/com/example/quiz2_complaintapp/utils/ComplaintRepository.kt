// utils/ComplaintRepository.kt
package com.example.quiz2_complaintapp.utils

import com.example.quiz2_complaintapp.models.Complaint
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ComplaintRepository {

    private val database = FirebaseDatabase.getInstance().getReference("Complaints")

    suspend fun submitComplaint(complaint: Complaint): Result<String> {
        return try {
            val id = database.push().key ?: return Result.failure(Exception("Could not generate ID"))
            val complaintWithId = complaint.copy(id = id)
            database.child(id).setValue(complaintWithId).await()
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getComplaintsFlow(): Flow<Result<List<Complaint>>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val complaints = mutableListOf<Complaint>()
                for (child in snapshot.children) {
                    val complaint = child.getValue(Complaint::class.java)
                    complaint?.let { complaints.add(it) }
                }
                // newest first
                complaints.sortByDescending { it.createdAt }
                trySend(Result.success(complaints))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Result.failure(Exception(error.message)))
            }
        }

        database.addValueEventListener(listener)
        awaitClose { database.removeEventListener(listener) }
    }

    suspend fun getComplaintById(id: String): Result<Complaint> {
        return try {
            val snapshot = database.child(id).get().await()
            val complaint = snapshot.getValue(Complaint::class.java)
                ?: return Result.failure(Exception("Not found"))
            Result.success(complaint)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}