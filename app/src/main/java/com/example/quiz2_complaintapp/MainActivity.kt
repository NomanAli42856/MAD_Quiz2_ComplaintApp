package com.example.quiz2_complaintapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quiz2_complaintapp.adapters.ComplaintAdapter
import com.example.quiz2_complaintapp.databinding.ActivityMainBinding
import com.example.quiz2_complaintapp.utils.Constants
import com.google.android.material.snackbar.Snackbar
import com.example.quiz2_complaintapp.models.Complaint

/**
 * Main screen: displays list of all complaints from Firestore in a RecyclerView.
 * Observes ComplaintListViewModel for real-time updates.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ComplaintListViewModel by viewModels()
    private lateinit var complaintAdapter: ComplaintAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupFab()
        observeViewModel()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "My Complaints"
    }

    private fun setupRecyclerView() {
        complaintAdapter = ComplaintAdapter(
            onItemClick = { complaint ->
                val intent = Intent(this, ComplaintDetailActivity::class.java).apply {
                    putExtra(Constants.EXTRA_COMPLAINT_ID, complaint.id)
                }
                startActivity(intent)
            },
            onEditClick = { complaint ->
                showEditDialog(complaint)
            },
            onDeleteClick = { complaint ->
                showDeleteConfirmation(complaint)
            }
        )

        binding.recyclerView.apply {
            adapter = complaintAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(false)
        }
    }

    private fun setupFab() {
        binding.fabAddComplaint.setOnClickListener {
            startActivity(Intent(this, RegisterComplaintActivity::class.java))
        }
    }

    private fun showDeleteConfirmation(complaint: Complaint) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Delete Complaint")
            .setMessage("Are you sure you want to delete \"${complaint.complaintTitle}\"?")
            .setPositiveButton("Delete") { _, _ ->
                val db = com.google.firebase.database.FirebaseDatabase
                    .getInstance().getReference("Complaints")
                db.child(complaint.id).removeValue()
                    .addOnSuccessListener {
                        com.google.android.material.snackbar.Snackbar
                            .make(binding.root, "Complaint deleted", com.google.android.material.snackbar.Snackbar.LENGTH_SHORT)
                            .show()
                    }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showEditDialog(complaint: Complaint) {
        val layout = android.widget.LinearLayout(this)
        layout.orientation = android.widget.LinearLayout.VERTICAL
        layout.setPadding(50, 30, 50, 10)

        val etTitle = android.widget.EditText(this)
        etTitle.hint = "Complaint Title"
        etTitle.setText(complaint.complaintTitle)

        val etDescription = android.widget.EditText(this)
        etDescription.hint = "Description"
        etDescription.setText(complaint.description)
        etDescription.minLines = 3

        layout.addView(etTitle)
        layout.addView(etDescription)

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Edit Complaint")
            .setView(layout)
            .setPositiveButton("Update") { _, _ ->
                val newTitle = etTitle.text.toString().trim()
                val newDesc = etDescription.text.toString().trim()

                if (newTitle.isNotEmpty() && newDesc.isNotEmpty()) {
                    val db = com.google.firebase.database.FirebaseDatabase
                        .getInstance().getReference("Complaints")
                    val updates = mapOf(
                        "complaintTitle" to newTitle,
                        "description" to newDesc
                    )
                    db.child(complaint.id).updateChildren(updates)
                        .addOnSuccessListener {
                            com.google.android.material.snackbar.Snackbar
                                .make(binding.root, "Updated successfully", com.google.android.material.snackbar.Snackbar.LENGTH_SHORT)
                                .show()
                        }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun observeViewModel() {
        // Observe loading state
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Observe complaints list
        viewModel.complaints.observe(this) { complaints ->
            complaintAdapter.submitList(complaints)

            // Show/hide empty state
            if (complaints.isEmpty()) {
                binding.layoutEmpty.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.layoutEmpty.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }

            // Update count badge
            binding.tvComplaintCount.text = "${complaints.size} complaint(s)"
        }

        // Observe errors
        viewModel.error.observe(this) { errorMsg ->
            errorMsg?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
        }
    }
}