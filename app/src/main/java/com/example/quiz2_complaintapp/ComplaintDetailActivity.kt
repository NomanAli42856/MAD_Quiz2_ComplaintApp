package com.example.quiz2_complaintapp

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.quiz2_complaintapp.databinding.ActivityComplaintDetailBinding
import com.example.quiz2_complaintapp.models.Complaint
import com.example.quiz2_complaintapp.utils.ComplaintRepository
import com.example.quiz2_complaintapp.utils.Constants
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Shows full details of a single complaint.
 * Receives the complaint ID via Intent extra and fetches it from Firestore.
 */
class ComplaintDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityComplaintDetailBinding
    private val repository = ComplaintRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComplaintDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        loadComplaint()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Complaint Details"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun loadComplaint() {
        val complaintId = intent.getStringExtra(Constants.EXTRA_COMPLAINT_ID)

        if (complaintId.isNullOrBlank()) {
            showError("Invalid complaint ID")
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        binding.scrollView.visibility = View.GONE

        lifecycleScope.launch {
            val result = repository.getComplaintById(complaintId)
            binding.progressBar.visibility = View.GONE

            result.fold(
                onSuccess = { complaint ->
                    binding.scrollView.visibility = View.VISIBLE
                    displayComplaint(complaint)
                },
                onFailure = { e ->
                    showError(e.message ?: "Failed to load complaint details")
                }
            )
        }
    }

    private fun displayComplaint(complaint: Complaint) {
        binding.apply {
            tvComplaintTitle.text = complaint.complaintTitle
            tvStudentName.text = complaint.studentName
            tvRollNumber.text = complaint.rollNumber
            tvCategory.text = "${Constants.getCategoryEmoji(complaint.category)}  ${complaint.category}"
            tvPriority.text = complaint.priority
            tvStatus.text = complaint.status
            tvDescription.text = complaint.description

            // Format and display the creation date
            val dateText = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                .format(java.util.Date(complaint.createdAt))
            tvCreatedAt.text = dateText
            // Color-code the priority label
            val priorityColorRes = Constants.getPriorityColorRes(complaint.priority)
            tvPriority.setTextColor(getColor(priorityColorRes))
        }
    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_INDEFINITE)
            .setAction("Go Back") { finish() }
            .show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}